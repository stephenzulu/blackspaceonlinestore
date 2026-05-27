
$(document).ready(function() {
    var table = $('#myTable').DataTable({
        responsive: true,
        lengthChange: false,
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'excelHtml5',
                text: 'Download Excel',
                className: 'btn btn-success',
                exportOptions: {
                    columns: [0, 1, 2, 3, 4 ]  // Export only ID, Name, and Country (skip Email)
                }
            }
        ],
        language: {
            search: ''
        },
        "pageLength": 5
    });

    // Move buttons and search box
    table.buttons().container().appendTo('#customButtons');
    $('#myTable_filter').appendTo('#customSearch').addClass('d-inline-block');
    $('#myTable_filter input').addClass('form-control form-control-sm ms-2').attr('placeholder', 'Search...');
});