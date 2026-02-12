create database joorjens_db DEFAULT character set utf8 DEFAULT COLLATE utf8_unicode_ci;

mysqldump -u user -p db_name > backup- file.sql
mysql -u root -p db_name < backup- file.sql

--write changes ...
ALTER TABLE store DROP FOREIGN KEY FK_STORE__AREA_areaCity;
ALTER TABLE store CHANGE COLUMN id_area_city id_area_zone BIGINT;
ALTER TABLE store ADD CONSTRAINT FK_STORE__AREA_areaZone FOREIGN KEY (id_area_zone) REFERENCES area (id);

ALTER TABLE distributor DROP COLUMN contractTypeDetail;
ALTER TABLE distributor DROP COLUMN contractTypeValue;
ALTER TABLE distributor DROP FOREIGN KEY FK_DISTRIBUTOR__OST__settlementType;
ALTER TABLE distributor DROP COLUMN id_settlement_type;

DROP TABLE product;

DROP TABLE distributor_product_price_history;
DROP TABLE distributor_product_price;
DROP TABLE distributor_product_discount;
DROP TABLE distributor_bundling_package_product;
DROP TABLE distributor_bundling_package;
DROP TABLE distributor_discount_package_product;
DROP TABLE distributor_discount_package;
DROP TABLE distributor_product;

DELETE FROM role__permission WHERE id_permission >= 3231 AND id_permission <= 3248;
DELETE FROM permission WHERE id >= 3231 AND id <= 3248;

ALTER TABLE vehicle DROP COLUMN color;
ALTER TABLE distributor DROP COLUMN activityType;
ALTER TABLE store DROP COLUMN activityType;
--UPDATE product_category_type set id = 1230 where id = 1210 and id_parent is NULL;

DROP TABLE valid_ip;
DELETE FROM order_status_type;

--for cart version
ALTER TABLE distributor_product_package CHANGE COLUMN count countFrom int DEFAULT 0;
ALTER TABLE distributor_product_package DROP COLUMN stock;
ALTER TABLE distributor_product_package DROP COLUMN stockWarn;
ALTER TABLE distributor_product_package DROP COLUMN minOrder;
DROP TABLE cart_distributor_package;
DROP TABLE cart_distributor;
DROP TABLE cart;
DELETE FROM order_status_type;
--
--ALTER TABLE favorite DROP INDEX UK_favorite_key,  ADD CONSTRAINT `UK_favorite_key` UNIQUE (`key_`,`id_rider`);

ALTER TABLE distributor DROP COLUMN maxDelivery;
ALTER TABLE distributor_product_discount CHANGE COLUMN priceOrCount offerCount int DEFAULT 0;
ALTER TABLE distributor_product_discount CHANGE COLUMN priceOrCountOrPercent percent float DEFAULT '0.0';


DROP TABLE distributor__oSettlementT;
DROP TABLE cart_distributor_packduct;
DROP TABLE cart_distributor;
DROP TABLE cart;
DROP TABLE order_settlement_type;

ALTER TABLE cart CHANGE COLUMN count count bigint(20) DEFAULT 0;
ALTER TABLE cart CHANGE COLUMN packCount packCount bigint(20) DEFAULT 0;
ALTER TABLE cart CHANGE COLUMN packPriceConsumer packPriceConsumer bigint(20) DEFAULT 0;
ALTER TABLE cart CHANGE COLUMN packPrice packPrice bigint(20) DEFAULT 0;
ALTER TABLE cart CHANGE COLUMN packPriceDiscount packPriceDiscount bigint(20) DEFAULT 0;

ALTER TABLE cart_distributor CHANGE COLUMN count count bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor CHANGE COLUMN packCount packCount bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor CHANGE COLUMN packPriceConsumer packPriceConsumer bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor CHANGE COLUMN packPrice packPrice bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor CHANGE COLUMN packPriceDiscount packPriceDiscount bigint(20) DEFAULT 0;

ALTER TABLE cart_distributor_packduct CHANGE COLUMN count count bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor_packduct CHANGE COLUMN packCount packCount bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor_packduct CHANGE COLUMN packPriceConsumer packPriceConsumer bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor_packduct CHANGE COLUMN packPrice packPrice bigint(20) DEFAULT 0;
ALTER TABLE cart_distributor_packduct CHANGE COLUMN packPriceDiscount packPriceDiscount bigint(20) DEFAULT 0;


DROP TABLE producer_rate;
DROP TABLE producer;

DROP TABLE store__activity_Type;
DROP TABLE distributor__activity_Type;
DROP TABLE activity_type;

ALTER TABLE cart_distributor_packduct DROP COLUMN amountCheck;
ALTER TABLE cart_distributor_packduct DROP COLUMN amountCache;
ALTER TABLE cart_distributor_packduct DROP COLUMN amountCredit;


ALTER TABLE message_receiver DROP FOREIGN KEY FK_MESSAGE_RECEIVER__CUSTOMER__receiver;
alter table message_receiver drop index UK_2aomc9m0ab9bp8fh8cv1oc5bv;

-- tirmah
drop table promotion;
update cart set timeFinished=updatedTime where finished=true;
update cart_distributor set timeFinished=updatedTime where finished=true;
update cart_distributor_packduct set timeFinished=updatedTime where finished=true;
ALTER TABLE cart DROP COLUMN finished;
ALTER TABLE cart_distributor DROP COLUMN finished;
ALTER TABLE cart_distributor_packduct DROP COLUMN finished;

select id_cart from cart_distributor where id_distributor is null;
delete from cart_distributor_packduct where id_cart_distributor in (select id from cart_distributor where id_distributor is null);
delete from cart_distributor where id_distributor is null;
delete from cart where id in (?,?);

ALTER TABLE banner CHANGE COLUMN link link varchar(255) DEFAULT NULL;
