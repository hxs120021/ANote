$("#register_").click(function zz(){
var loginname = $('#use').val();
var loginpsd = $('#psd').val();
var jsonL = {"loginname":loginname,"loginpsd":loginpsd};
var jsonArrayFinal = JSON.stringify(jsonL);

$.ajax({
        cache:true,
        type:"POST",
        url:'http://10.3.21.199:8080/UserServlet',
        dataType:'jsonp',//处理跨域问题
        jsonp: "callback",
        data: {method: "regist","list":jsonArrayFinal},
        async:  false,
        success:function a(data){
                                    var strJSON = JSON.stringify(data);
                                    var obj = eval( "(" + strJSON + ")" );//转换后的JSON对象
                                    if(obj.ok === "success"){
                                        alert("注册成功！")
                                    }else{
                                        alert("已被注册！");
                                    }
                                },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                alert(textStatus);
             }
    });
})

$("#login_").click(function s(){
var loginname = $('#input-4').val();
var loginpsd = $('#input-5').val();
var jsonL = {"loginname":loginname,"loginpsd":loginpsd};
var jsonArrayFinal = JSON.stringify(jsonL);

$.ajax({
        cache:true,
        type:"get",
        url:'http://10.3.21.199:8080/UserServlet',
        dataType:'jsonp',//处理跨域问题
        jsonp: "callback",
        data: {method: "login","list":jsonArrayFinal},
        async:  false,
        success:function a(data){
                var strJSON = JSON.stringify(data);
                var obj = eval( "(" + strJSON + ")" );//转换后的JSON对象
                if(obj.ok === "success"){
                    window.submitacc.toActivity(loginname);
                }else{
                    alert("error!!");
                }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                alert(textStatus);
             }
    });
})