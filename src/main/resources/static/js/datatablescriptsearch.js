$(document).ready(function () {
    // Initialize DataTable without default search bar
    var table = $('#myTable').DataTable({
        dom: 'lrtip', // hides default search
        responsive: true,
        pageLength: 5,       // Force 5 rows per page
        lengthChange: false  // Hide "Show 10 entries" dropdown
    });

    // Apply custom search
    $('#customSearchBox').on('keyup', function () {
        table.search(this.value).draw();
    });
});

$(document).ready(function () {
    // Initialize DataTable without default search bar
    var table = $('#myTable1').DataTable({
        dom: 'lrtip', // hides default search
        responsive: true,
        pageLength: 5,       // Force 5 rows per page
        lengthChange: false  // Hide "Show 10 entries" dropdown
    });

    // Apply custom search
    $('#customSearchBox1').on('keyup', function () {
        table.search(this.value).draw();
    });
});


$(document).ready(function () {
    // Initialize DataTable without default search bar
    var table = $('#myTable2').DataTable({
        dom: 'lrtip', // hides default search
        responsive: true,
        pageLength: 5,       // Force 5 rows per page
        lengthChange: false  // Hide "Show 10 entries" dropdown
    });

    // Apply custom search
    $('#customSearchBox2').on('keyup', function () {
        table.search(this.value).draw();
    });
});


$(document).ready(function () {
    // Initialize DataTable without default search bar
    var table = $('#myTable3').DataTable({
        dom: 'lrtip', // hides default search
        responsive: true,
        pageLength: 5,       // Force 5 rows per page
        lengthChange: false  // Hide "Show 10 entries" dropdown
    });

    // Apply custom search
    $('#customSearchBox3').on('keyup', function () {
        table.search(this.value).draw();
    });
});

