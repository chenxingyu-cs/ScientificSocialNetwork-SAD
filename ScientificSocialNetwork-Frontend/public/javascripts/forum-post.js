$(function(){
    $(".hide-hood").hide();
    $(".toggle").click(function(){
        var hood = $(this).parent().children(".hide-hood").toggle();
        hood.children("input").focus();
    })

    $(".vote-thumb").click(function(){
        var commentId = $(this).attr("data-commentId");
        var act = $(this).attr("data-act");
        var self = $(this)
        if (act === "voteup") {
            var url = "/forum/thumbUp/"+commentId;
            $.getJSON(url, {}, function(data){
                var vote_num = self.parent().children(".vote-num");
                var number = $(vote_num[0]).text();
                $(vote_num[0]).text(Number.parseInt(number)+1);
                self.parent().children(".vote-thumb").removeClass("voted");
                self.addClass("voted")
            });
        } else {
            var url = "/forum/thumbDown/"+commentId;
            $.getJSON(url, {}, function(data){
                var vote_num = self.parent().children(".vote-num");
                var number = $(vote_num[0]).text();
                $(vote_num[0]).text(Number.parseInt(number)-1);
                self.parent().children(".vote-thumb").removeClass("voted");
                self.addClass("voted")
            });
        }
    })

});