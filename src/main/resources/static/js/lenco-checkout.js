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
                verifyOnServer(response.reference);
            },
            onClose: function () {
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