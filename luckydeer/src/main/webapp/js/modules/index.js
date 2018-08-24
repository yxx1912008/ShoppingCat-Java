/* 项目主入口*/
layui.define(['layer', 'form'], function (exports) {
    var layer = layui.layer, form = layui.form;
    /*调用jquery*/
    var $ = layui.jquery;
    /*加载层*/
    var loader = layer.load(2, {time: 10 * 1000});

    $.ajax({
        type: 'POST',
        url: 'wechat/cat/getIndexPoster.wx',
        success: function (msg) {
            var str = "";
            for (var i = 0; i < msg.data.length; i++) {
                str += ' <div class="swiper-slide"><img class="index_poster" src="' + msg.data[i].imgUrl + '" alt=""></div>';
            }
            $('.swiper-wrapper').append(str);
            var main_swiper = new Swiper('.swiper-container', {
                initialSlide: 0,
                observer: true,//修改swiper自己或子元素时，自动初始化swiper
                observeParents: true,//修改swiper的父元素时，自动初始化swiper,
                // 如果需要分页器
                pagination: {
                    el: '.swiper-pagination',
                }
            });
            layer.close(loader);
        }, error: function () {
            layer.msg('页面加载失败');
            return;
        }
    });
    exports('index', {});
});



