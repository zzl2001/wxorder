<html>
<#include "../common/header.ftl">
    <body>
    <div id="wrapper" class="toggled">
        <#--边栏 sidebar-->
            <#include "../common/nav.ftl">
        <#--主要内容content-->
        <div id = "page-content-wrapper">
            <div class="container-fluid">
                <div class="row clearfix">
                    <div class="col-md-12 column">
                        <div class="row clearfix">
                            <div class="col-md-12 column">
                                <nav class="navbar navbar-default" role="navigation">
                                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                                        <form class="navbar-form navbar-left" role="search" action="search">
                                            <div class="form-group">
                                                <input type="text" class="form-control" id="orderId" name="orderId"/>
                                            </div>
                                            <button class="btn btn-default">查询</button>
                                        </form>
                                    </div>
                                </nav>
                                <table class="table table-bordered table-condensed">
                                    <thead>
                                    <tr>
                                        <th>订单id</th>
                                        <th>姓名</th>
                                        <th>手机号</th>
                                        <th>地址</th>
                                        <th>金额</th>
                                        <th>订单状态</th>
                                        <th>支付状态</th>
                                        <th>创建时间</th>
                                        <th colspan="2">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list orderDTOpage.content as orderDTO>
                                    <tr>
                                        <td>${orderDTO.getOrderId()}</td>
                                        <td>${orderDTO.getBuyerName()}</td>
                                        <td>${orderDTO.getBuyerPhone()}</td>
                                        <td>${orderDTO.getBuyerAddress()}</td>
                                        <td>${orderDTO.getOrderAmount()}</td>
                                        <td>${orderDTO.getOrderStatusEnum().getMsg()}</td>
                                        <td>${orderDTO.getPayStatusEnum().getMsg()}</td>
                                        <td>${orderDTO.getCreateTime()}</td>
                                        <td>
                                            <a href="/sell/seller/order/detail?orderId=${orderDTO.orderId}">详情</a>
                                        </td>
                                        <td>
                                            <#if orderDTO.getOrderStatusEnum().getMsg() =="新订单">
                                                <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}">取消</a>
                                            </#if>
                                        </td>
                                    </tr>
                                    </#list>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 column">
                        <ul class="pagination pull-right" >
                    <#if currentPage lte 1>
                    <li class="disabled"><a href="#">上一页</a></li>
                    <#else >
                    <li><a href="/sell/seller/order/list?page=${currentPage-1}&size=${currentSize}">上一页</a></li>
                    </#if>
                     <#if (orderDTOpage.getTotalPages()>10)>
                      <li><a href="/sell/seller/order/list?page=${1}&size=${currentSize}">${1}</a></li>
                        <li><a>${"..."}</a></li>
                       <li><a href="/sell/seller/order/list?page=${currentPage}&size=${currentSize}">${currentPage}</a></li>
                        <li><a>${"..."}</a></li>
                        <li><a href="/sell/seller/order/list?page=${orderDTOpage.getTotalPages()}&size=${currentSize}">${orderDTOpage.getTotalPages()}</a></li>
                     <#--没写好-->
                     <#else >
                         <#list 1..orderDTOpage.getTotalPages() as index>
                             <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                             <#else>
                                 <li><a href="/sell/seller/order/list?page=${index}&size=${currentSize}">${index}</a></li>
                             </#if>
                         </#list>
                     </#if>
                    <#if currentPage == orderDTOpage.getTotalPages()>
                    <li class="disabled"><a href="#">下一页</a></li>
                    <#else >
                        <li><a href="/sell/seller/order/list?page=${currentPage+1}&size=${currentSize}">下一页</a></li>
                    </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>
