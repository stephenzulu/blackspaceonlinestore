/*
 * Lenco checkout — served as a static resource by Spring Boot (/js/lenco-checkout.js).
 *
 * Flow:
 *   1. Collect form input and open the Lenco widget with LencoPay.getPaid(...).
 *   2. On success, send the reference to the backend (GET /api/payments/verify).
 *   3. The backend calls Lenco with the SECRET key and returns a trimmed result.
 *   4. Redirect to /success only when the backend confirms the payment.
 *
 * The public key + currency come from window.LENCO_CONFIG, injected by the template.
 */
(function () {
    "use strict";

    var config = window.LENCO_CONFIG || {};

    function setStatus(message) {
        var el = document.getElementById("status");
        if (el) el.textContent = message;
    }

    // Map the dropdown to the widget's `channels` array.
    function selectedChannels() {
        var value = document.getElementById("channel").value;
        if (value === "card") return ["card"];
        if (value === "both") return ["card", "mobile-money"];
        return ["mobile-money"];
    }

    // A unique reference per attempt (only -, ., _ and alphanumerics are allowed).
    function newReference() {
        return "ref-" + Date.now() + "-" + Math.random().toString(36).slice(2, 8);
    }

    // Ask our server whether the payment really went through. Never trust the
    // browser callback alone — the secret key stays server-side.
    function verifyOnServer(reference) {
        setStatus("Verifying payment…");
        fetch("/api/payments/verify?reference=" + encodeURIComponent(reference))
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (data.verified) {
                    window.location = "/success?ref=" + encodeURIComponent(data.reference);
                } else {
                    setStatus("Payment not confirmed (status: " + data.status + ").");
                }
            })
            .catch(function () {
                setStatus("Could not verify payment. If you were charged, please contact support.");
            });
    }

    function startPayment() {
        if (typeof LencoPay === "undefined") {
            setStatus("Payment library failed to load. Please refresh and try again.");
            return;
        }
        if (!config.publicKey || config.publicKey === "YOUR_PUBLIC_KEY") {
            setStatus("Missing Lenco public key. Set LENCO_PUBLIC_KEY on the server.");
            return;
        }

        var amount = Number(document.getElementById("amount").value);
        if (!amount || amount <= 0) {
            setStatus("Enter a valid amount.");
            return;
        }

        setStatus("Opening payment window…");

        function preventRefresh(e) {
            e.preventDefault();
            e.returnValue = 'Payment is in progress. Are you sure you want to leave?';
        }
        window.addEventListener('beforeunload', preventRefresh);

        var overlay = document.createElement('div');
        overlay.id = 'payment-overlay';
        overlay.style.cssText = 'position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.6);z-index:9999;display:flex;align-items:center;justify-content:center;';
        overlay.innerHTML = '<div style="background:#fff;padding:30px 40px;border-radius:15px;text-align:center;max-width:400px;">'
            + '<div style="font-size:2.5rem;margin-bottom:10px;">&#9888;&#65039;</div>'
            + '<h3 style="margin:0 0 10px;">Payment in Progress</h3>'
            + '<p style="color:#666;">Please <strong>do not refresh</strong> or close this page.<br>Wait while your payment is being processed.</p>'
            + '<div style="margin-top:15px;"><div style="width:40px;height:40px;border:4px solid #eee;border-top:4px solid #E8611D;border-radius:50%;animation:spin 1s linear infinite;margin:0 auto;"></div></div>'
            + '</div>';
        var style = document.createElement('style');
        style.textContent = '@keyframes spin{0%{transform:rotate(0)}100%{transform:rotate(360deg)}}';
        document.head.appendChild(style);
        document.body.appendChild(overlay);

        function removeOverlay() {
            window.removeEventListener('beforeunload', preventRefresh);
            var el = document.getElementById('payment-overlay');
            if (el) el.remove();
        }

        LencoPay.getPaid({
            key: config.publicKey,
            reference: newReference(),
            email: document.getElementById("email").value,
            amount: amount,                       // plain number, e.g. 10.75 — not the lowest unit
            currency: config.currency || "ZMW",
            channels: selectedChannels(),
            customer: {
                firstName: document.getElementById("firstName").value,
                lastName: document.getElementById("lastName").value,
                phone: document.getElementById("phone").value
            },
            onSuccess: function (response) {
                removeOverlay();
                verifyOnServer(response.reference);
            },
            onClose: function () {
                removeOverlay();
                setStatus("Payment window closed before completion.");
            },
            onConfirmationPending: function () {
                setStatus("Your purchase will complete once the payment is confirmed.");
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        var button = document.getElementById("payButton");
        if (button) button.addEventListener("click", startPayment);
    });
})();