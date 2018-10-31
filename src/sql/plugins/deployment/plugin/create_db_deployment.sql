DROP TABLE IF EXISTS deployment_application;

--
-- Table structure for table deployment_application
--
CREATE TABLE deployment_application (
	id_application int default 0 NOT NULL,
	code varchar(100) default NULL,
	name varchar(100) default NULL,
	url_site varchar(256) default NULL,
	webapp_name varchar(100) default NULL,
	workgroup varchar(100) default NULL,
	repo_type varchar(100) default NULL,
	artifact_id varchar(100) default NULL,
	is_site_lutece SMALLINT default 1,
	maven_custom_deploy_goal varchar(1000) default '',
	PRIMARY KEY (id_application)
);

