$('document').ready(function(){
    $('table #editButton').on('click',function(event){
        event.preventDefault();
        var href = $(this).attr('href');

        $.get(href,function (category,status){
            $('#editId').val(category.id);
            $('#editName').val(category.name);
        });

        $('#EditCategory').modal();
    });
});