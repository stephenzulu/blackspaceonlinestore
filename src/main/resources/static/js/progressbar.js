document.addEventListener("DOMContentLoaded", function () {
    const loader = document.getElementById("progressContainer");

    // Simulate "progress" before hiding
    setTimeout(() => {
        loader.style.opacity = "0";
        setTimeout(() => loader.style.display = "none", 200); // fade-out delay
    }, 800); // keep spinner visible for 1.5s
});




    flatpickr("#tdate", {
    dateFormat: "d F Y",
    defaultDate: "2025-06-30",
    minDate: "today"// Example: 2025-06-17
});

    flatpickr("#ttime", {
    enableTime: true,
    noCalendar: true,
    dateFormat: "H:i", // Example: 14:30
    time_24hr: true
});
