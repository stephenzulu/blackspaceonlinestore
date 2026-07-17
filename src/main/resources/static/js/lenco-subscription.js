function newReference() {
    var chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var rand = '';
    for (var i = 0; i < 10; i++) {
        rand += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return 'ref-' + Date.now() + '-' + rand;
}

function selectPlan(subscriptionId) {
    var config = window.LENCO_CONFIG;
    if (!config || !config.publicKey) {
        Swal.fire('Error', 'Payment system is not configured.', 'error');
        return;
    }

    fetch('/api/subscription/select', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ subscriptionId: subscriptionId })
    })
    .then(function(res) { return res.json(); })
    .then(function(data) {
        if (data.error) {
            Swal.fire('Error', data.error, 'error');
            return;
        }

        var reference = newReference();
        var amount = parseFloat(data.amount);

        if (typeof LencoPay === 'undefined') {
            Swal.fire('Error', 'Payment widget failed to load. Please refresh the page.', 'error');
            return;
        }

        LencoPay.getPaid({
            key: config.publicKey,
            reference: reference,
            email: config.email,
            amount: amount,
            currency: config.currency,
            channels: ['card', 'mobile-money'],
            customer: {
                firstName: config.firstName,
                lastName: config.lastName,
                phone: config.phone
            },
            onSuccess: function(response) {
                console.log('Lenco onSuccess response:', JSON.stringify(response));

                Swal.fire({
                    title: 'Processing payment...',
                    allowOutsideClick: false,
                    didOpen: function() { Swal.showLoading(); }
                });

                var ref = response.reference || reference;

                fetch('/api/payments/complete', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ reference: ref })
                })
                .then(function(res) { return res.json(); })
                .then(function(result) {
                    if (result.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Subscription Upgraded!',
                            text: 'Your subscription has been activated successfully.',
                            timer: 2000,
                            showConfirmButton: false
                        }).then(function() {
                            window.location.href = '/account/subscription';
                        });
                    } else {
                        Swal.fire('Error', result.status || 'Something went wrong.', 'error');
                    }
                })
                .catch(function() {
                    Swal.fire('Error', 'Could not complete payment. Please contact support.', 'error');
                });
            },
            onClose: function() {
                Swal.fire('Cancelled', 'Payment was cancelled.', 'info');
            },
            onConfirmationPending: function() {
                Swal.fire('Pending', 'Payment is being confirmed. You will be notified once complete.', 'info');
            }
        });
    })
    .catch(function() {
        Swal.fire('Error', 'Failed to initiate payment. Please try again.', 'error');
    });
}
