$(function(){
    $(".hide-hood").hide();
    $(".toggle").click(function(){
        var hood = $(this).parent().children(".hide-hood").toggle();
        hood.children("input").focus();
    })

    $(".vote-thumb").click(function(){
        var id = $(this).attr("data-commentId");
        var act = $(this).attr("data-act");
        var self = $(this)
        if (act === "voteup") {
            var url = "/publication/thumbUp/"+id;
            $.getJSON(url, {}, function(data){
                var vote_num = self.parent().children(".vote-num");
                var number = $(vote_num[0]).text();
                $(vote_num[0]).text(Number.parseInt(number)+1);
                self.parent().children(".vote-thumb").removeClass("voted");
                self.addClass("voted")
            });
        } else {
            var url = "/publication/thumbDown/"+id;
            $.getJSON(url, {}, function(data){
                var vote_num = self.parent().children(".vote-num");
                var number = $(vote_num[0]).text();
                $(vote_num[0]).text(Number.parseInt(number)-1);
                self.parent().children(".vote-thumb").removeClass("voted");
                self.addClass("voted")
            });
        }
    })
    
    $("#comment").click(function () {
        var content = $("#content").value();
        if(content == "") {
            $("#alert").addClass("in");
        }
    })

});