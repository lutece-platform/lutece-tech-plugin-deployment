--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES ('DEPLOYMENT_MANAGEMENT','deployment.adminFeature.deployment_management.name',0,'jsp/admin/plugins/deployment/ManageApplication.jsp','deployment.adminFeature.deployment_management.description',0,'deployment','MANAGERS','images/admin/skin/plugins/profiles/deployment.png', NULL);

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('DEPLOYMENT_MANAGEMENT',1);

--
-- Init  table core_attibute
--
INSERT INTO core_attribute (id_attribute,type_class_name, title, help_message, is_mandatory, plugin_name) VALUES (1,'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'Login SVN','',0,'deployment');
INSERT INTO core_attribute (id_attribute,type_class_name, title, help_message, is_mandatory, plugin_name) VALUES (2,'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'Password SVN','',0,'deployment');


--
-- Init  table core_attibute_field
--
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (1,1,NULL,NULL,0,0,20,50,0,1);
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (2,2,NULL,NULL,0,0,20,50,0,2);

--
-- Init table core_admin_role
--
INSERT INTO core_admin_role (role_key, role_description) VALUES ( 'admin_deployment','Administation des deploiements');

INSERT INTO core_admin_role_resource ( rbac_id, role_key, resource_type, resource_id, permission ) VALUES 
( 1200,'admin_deployment','DEPLOYMENT_APPLICATION','*','CREATE')
,( 1201,'admin_deployment','DEPLOYMENT_APPLICATION','*','DELETE')
,( 1202,'admin_deployment','DEPLOYMENT_APPLICATION','*','MODIFY')
,( 1203,'admin_deployment','DEPLOYMENT_APPLICATION','*','DEPLOY_APPLICATION')
,( 1204,'admin_deployment','DEPLOYMENT_APPLICATION','*','DEPLOY_SCRIPT')
,( 1205,'admin_deployment','DEPLOYMENT_APPLICATION','*','INIT_DATABASE')
,( 1206,'admin_deployment','DEPLOYMENT_APPLICATION','*','INIT_APP_CONTEXT')
,( 1207,'admin_deployment','DEPLOYMENT_ENVIRONMENT','*','DEPLOY_ON_ENVIROMENT');

INSERT INTO core_user_role (role_key,id_user) VALUES ( 'admin_deployment',1);

UPDATE core_admin_user SET password_max_valid_date = NULL WHERE id_user = 1;

