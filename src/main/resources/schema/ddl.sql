create table request_log
(
    id              bigint primary key auto_increment comment 'ID',
    name            varchar comment '接口在日志中的名称',
    method_name     varchar comment '接口对应的方法全名',
    request_ip      varchar comment '发起请求的 IP 地址',
    request_uri     varchar comment '请求地址',
    http_method     varchar_ignorecase comment '发出此请求的 HTTP 方法的名称',
    token           varchar comment 'Token（如果有的话）',
    headers         varchar comment '请求头信息',
    request_payload varchar comment '接口请求参数',
    response_result varchar comment '接口响应结果',
    request_time    timestamp comment '接口开始执行的时间戳',
    response_time   timestamp comment '接口结束执行的时间戳',
    error           boolean comment '是否异常',
    error_message   varchar comment '接口调用发送异常的异常信息',
    created_by      varchar comment '请求的发起人'
);