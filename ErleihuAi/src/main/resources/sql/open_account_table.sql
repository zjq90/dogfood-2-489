-- 开户信息表创建SQL
CREATE TABLE IF NOT EXISTS `open_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `clilent_no` varchar(50) DEFAULT NULL COMMENT '商户编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `account_no` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `open_status` char(1) DEFAULT '0' COMMENT '开户状态：0-开户中，1-开户成功，2-开户失败',
  `cert_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `msg` varchar(500) DEFAULT NULL COMMENT '信息描述',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `otp_order_no` varchar(50) DEFAULT NULL COMMENT '短信订单号',
  `business_no` varchar(50) DEFAULT NULL COMMENT '短信业务号',
  `ele_account_no` varchar(30) DEFAULT NULL COMMENT '电子账户号',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP',
  `home_address` varchar(200) DEFAULT NULL COMMENT '家庭地址',
  `lbs` varchar(50) DEFAULT NULL COMMENT '经纬度',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备标识号',
  `agreement_no` varchar(100) DEFAULT NULL COMMENT '协议编号',
  `open_order_no` varchar(50) DEFAULT NULL COMMENT '开户订单号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_cert_no` (`cert_no`),
  KEY `idx_business_no` (`business_no`),
  KEY `idx_client_no` (`clilent_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开户信息表';

-- 电子账户信息表创建SQL
CREATE TABLE IF NOT EXISTS `ele_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `clilent_no` varchar(50) DEFAULT NULL COMMENT '商户编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `account_no` varchar(30) DEFAULT NULL COMMENT '绑定银行卡号',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `cert_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `ele_account_no` varchar(30) DEFAULT NULL COMMENT '电子账户号（二类户账号）',
  `agreement_no` varchar(100) DEFAULT NULL COMMENT '协议编号',
  `add_date` varchar(10) DEFAULT NULL COMMENT '添加日期',
  `add_time` varchar(10) DEFAULT NULL COMMENT '添加时间',
  `ele_account_status` char(1) DEFAULT '1' COMMENT '电子账户状态：1-有效，0-无效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_cert_no` (`cert_no`),
  UNIQUE KEY `idx_ele_account_no` (`ele_account_no`),
  KEY `idx_client_no` (`clilent_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='电子账户信息表';
