$(document).ready(function () {
    $(".btn-message").click(function () {
        $(".message-form").slideToggle("slow");

    });

    $(".btn-comment").click(function () {
        $(".comment").slideToggle("normal", function () {
            var position = $(".stars").offset().top;
            $("html,body").animate({
                scrollTop: position
            }, 1000);
        });


    });




});