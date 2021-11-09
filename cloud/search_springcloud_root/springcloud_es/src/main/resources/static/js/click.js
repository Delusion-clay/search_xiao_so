$(function(){
    //初始化页码
    var page = window.localStorage.getItem("pageN");
    if(page==null) window.localStorage.setItem("pageN","1");
    nextPage(25);
    function nextPage(pageSize){
        var pageN = window.localStorage.getItem("pageN");
        $.ajax({
            method:"post",
            url:"http://localhost:8002/es/searchAll",
            data:{
                indexName:"target_all",
                pageNumber:pageN,
                pageSize:pageSize
            },
            success(data){
                //创建DOM生成器盒子
                var documentFragment = document.createDocumentFragment();
                data.forEach(function(items){
                    //创建一个div
                    var line = document.createElement("div");
                    //添加类名
                    line.setAttribute("class","threadlist_line");
                    //创建div
                    var avatar = document.createElement("div");
                    avatar.setAttribute("class","threadlist_avatar");
                    var avatar_img = document.createElement("div");
                    avatar_img.setAttribute("class","threadlist_avatar_img");
                    var img = document.createElement("img");
                    //img.setAttribute("src","images/baidu.ico");img.setAttribute("src","images/baidu.ico");
                    if (items.infotype === "baidu"){
                        img.setAttribute("src","images/baidu.ico");
                    }
                    if (items.infotype==="douyin"){
                        img.setAttribute("src","images/douyin.ico");
                    }
                    if (items.infotype==="36ke"){
                        img.setAttribute("src","images/36ke.ico");
                    }
                    if (items.infotype==="baijiahao"){
                        img.setAttribute("src","images/baijiahao.ico");
                    }
                    if (items.infotype==="bili"){
                        img.setAttribute("src","images/bili.ico");
                    }if (items.infotype==="chouti"){
                        img.setAttribute("src","images/chouti.png");
                    }
                    if (items.infotype==="gongzhonghao"){
                        img.setAttribute("src","images/gongzhonghao.ico");
                    }
                    if (items.infotype==="guanchazhe"){
                        img.setAttribute("src","images/guancha.ico");
                    }
                    if (items.infotype==="hupu"){
                        img.setAttribute("src","images/hupu.ico");
                    }
                    if (items.infotype==="pengpai"){
                        img.setAttribute("src","images/peng.png");
                    }
                    if (items.infotype==="shuimu"){
                        img.setAttribute("src","images/shuimu.jpg");
                    }if (items.infotype==="toutiao"){
                        img.setAttribute("src","images/toutiao.ico");
                    }
                    if (items.infotype==="weibo"){
                        img.setAttribute("src","images/weibo.ico");
                    }if (items.infotype==="xinjingbao"){
                        img.setAttribute("src","images/xinjingbao.jpg");
                    }
                    if (items.infotype==="xinlang"){
                        img.setAttribute("src","images/xinlang.ico");
                    }
                    if (items.infotype ==="zhihu"){
                        img.setAttribute("src","images/zhihu.ico");
                    }
                    avatar_img.appendChild(img);
                    avatar.appendChild(avatar_img);
                    line.appendChild(avatar);

                    var content = document.createElement("div");
                    content.setAttribute("class","threadlist_content");

                    var subject = document.createElement("div");
                    subject.setAttribute("class","threadlist_subject");

                    var nofollow = document.createElement("a");
                    nofollow.setAttribute("href",items.url);
                    nofollow.setAttribute("target","_blank");
                    nofollow.textContent = items.title;

                    subject.appendChild(nofollow);
                    content.appendChild(subject);

                    var small = document.createElement("div");
                    small.setAttribute("class","gray small");
                    small.textContent = items.infomessage;

                    content.appendChild(small);
                    line.appendChild(content);

                    documentFragment.append(line);
                });
                $(".topN").html("");
                $(".topN").append(documentFragment);

            }
        });
    }
    $(".from_submit").click(function () {
        var pageN = window.localStorage.getItem("pageN");
        var keyWord = $("#wd").val();
        console.log(keyWord);
        $.ajax({
            method: "post" ,
            url:"http://localhost:8002/es/searchByWord",
            data:{
                pageNumber:pageN,
                pageSize:25,
                keyWord:keyWord
            },
            success(data){
                //创建DOM生成器盒子
                var documentFragment = document.createDocumentFragment();
                data.forEach(function(items){
                    //创建一个div
                    var line = document.createElement("div");
                    //添加类名
                    line.setAttribute("class","threadlist_line");
                    //创建div
                    var avatar = document.createElement("div");
                    avatar.setAttribute("class","threadlist_avatar");
                    var avatar_img = document.createElement("div");
                    avatar_img.setAttribute("class","threadlist_avatar_img");
                    var img = document.createElement("img");
                    //img.setAttribute("src","images/baidu.ico");img.setAttribute("src","images/baidu.ico");
                    if (items.infotype === "baidu"){
                        img.setAttribute("src","images/baidu.ico");
                    }
                    if (items.infotype==="douyin"){
                        img.setAttribute("src","images/douyin.ico");
                    }
                    if (items.infotype==="36ke"){
                        img.setAttribute("src","images/36ke.ico");
                    }
                    if (items.infotype==="baijiahao"){
                        img.setAttribute("src","images/baijiahao.ico");
                    }
                    if (items.infotype==="bili"){
                        img.setAttribute("src","images/bili.ico");
                    }if (items.infotype==="chouti"){
                        img.setAttribute("src","images/chouti.png");
                    }
                    if (items.infotype==="gongzhonghao"){
                        img.setAttribute("src","images/gongzhonghao.ico");
                    }
                    if (items.infotype==="guanchazhe"){
                        img.setAttribute("src","images/guancha.ico");
                    }
                    if (items.infotype==="hupu"){
                        img.setAttribute("src","images/hupu.ico");
                    }
                    if (items.infotype==="pengpai"){
                        img.setAttribute("src","images/peng.png");
                    }
                    if (items.infotype==="shuimu"){
                        img.setAttribute("src","images/shuimu.jpg");
                    }if (items.infotype==="toutiao"){
                        img.setAttribute("src","images/toutiao.ico");
                    }
                    if (items.infotype==="weibo"){
                        img.setAttribute("src","images/weibo.ico");
                    }if (items.infotype==="xinjingbao"){
                        img.setAttribute("src","images/xinjingbao.jpg");
                    }
                    if (items.infotype==="xinlang"){
                        img.setAttribute("src","images/xinlang.ico");
                    }
                    if (items.infotype ==="zhihu"){
                        img.setAttribute("src","images/zhihu.ico");
                    }
                    avatar_img.appendChild(img);
                    avatar.appendChild(avatar_img);
                    line.appendChild(avatar);

                    var content = document.createElement("div");
                    content.setAttribute("class","threadlist_content");

                    var subject = document.createElement("div");
                    subject.setAttribute("class","threadlist_subject");

                    var nofollow = document.createElement("a");
                    nofollow.setAttribute("href",items.url);
                    nofollow.setAttribute("target","_blank");
                    nofollow.textContent = items.title;

                    subject.appendChild(nofollow);
                    content.appendChild(subject);

                    var small = document.createElement("div");
                    small.setAttribute("class","gray small");
                    small.textContent = items.infomessage;

                    content.appendChild(small);
                    line.appendChild(content);

                    documentFragment.append(line);
                });
                $(".topN").html("");
                $(".topN").append(documentFragment);

            }
        });
    });

    function pageTo(type){
        switch (type) {
            case "pre":{
                var prePage = window.localStorage.getItem("pageN");
                if ((prePage*1) === 1){
                    alert("不要再点了，到头啦");
                    return;
                }
                var prePageInt = prePage*1-1;
                window.localStorage.setItem("pageN",prePageInt+"");
                break;
            }
            case "next":{
                var nextPage = window.localStorage.getItem("pageN");
                var nextPageInt = nextPage*1+1;
                window.localStorage.setItem("pageN",nextPageInt+"");
                break;
            }
        }
    }

    $(".multi>a.next").on("click",function () {
        pageTo("next");
        nextPage(25);
    });

    $(".multi>a.pre").on("click",function () {
        pageTo("pre");
        nextPage(25);
    })
});