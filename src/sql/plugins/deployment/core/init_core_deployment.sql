--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES ('DEPLOYMENT_MANAGEMENT','deployment.adminFeature.deployment_management.name',0,'jsp/admin/plugins/deployment/ManageApplication.jsp','deployment.adminFeature.deployment_management.description',0,'deployment','MANAGERS','images/admin/skin/plugins/profiles/deployment.png', NULL);

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('PROFILES_MANAGEMENT',1);

--
-- Init  table core_attibute
--
INSERT INTO core_attribute (id_attribute,type_class_name, title, help_message, is_mandatory, plugin_name) VALUES (1,'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'Login SVN','',1,'deployment');
INSERT INTO core_attribute (id_attribute,type_class_name, title, help_message, is_mandatory, plugin_name) VALUES (2,'fr.paris.lutece.portal.business.user.attribute.AttributeText', 'Password SVN','',1,'deployment');


--
-- Init  table core_attibute_field
--
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (1,1,NULL,NULL,0,0,20,0,0,1);
INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) VALUES (2,2,NULL,NULL,0,0,20,0,0,1);
