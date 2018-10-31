var _refresh;
var delay = 2000; // might be useless...

function setResultLog( log ){
	logArea = document.getElementById( "pre_log" );
	logArea.value = log;
	logArea.scrollTop = logArea.scrollHeight;
}

function setResultInformations( resultInformations ){
	if(resultInformations.dump_file_url!=null && resultInformations.dump_file_url!= ""){
		$('#dump_file_url').html(" <a class='btn btn-primary btn-flat btn-block' href='"+resultInformations.dump_file_url+"' title='Télécharger le dump de sauvegarde' > <i class='glyphicon glyphicon-download'></i></a> ");
		$('#dump_file').show();
	}
}

firstRepoUrlHelp = $("#url_repo_help").text();
function checkRepository( )
{
    $('#url_repo').on('input',function(e)
    {
        if ( $("#url_repo").val()==="" )
        {
            $('#url_repo_help').text(firstRepoUrlHelp);
            $('#group_repo_url').removeClass( 'has-success' );
            $('#group_repo_url').removeClass( 'has-error' );
        }
        else
        {
           $.post(
           'jsp/admin/plugins/deployment/CheckRepository.jsp',
           { url_repo : $('#url_repo').val()},
           processCheckRepository,
           'json'
            ); 
        }
        
    });
}



function processCheckRepository ( strJson )
{
    
    if ( strJson.valid_url )
    {
        $('#group_repo_url').removeClass( 'has-error' );
        $('#group_repo_url').addClass( 'has-success' );
        var fontIcon = "";
        var repoType = "";
        switch ( strJson.repo_type )
        {
            case 'gitlab':
                repoType = "GitLab";
                break;
            case 'svn':
                repoType = "SubVersioN";
                break;
            case 'github':
                repoType = "GitHub";
                break;
        }
        $('#url_repo_help').text("Repository : "+repoType+".");
    }
    else
    {
        $('#group_repo_url').removeClass( 'has-success' );
        $('#group_repo_url').addClass( 'has-error' );
        $('#url_repo_help').text(firstRepoUrlHelp);
    }
    
        
}

function setErrors( errors,errorType){
	if(errors!=null && errors!= "")	{
		$('#errors_msg').html( errors);
	}
	$('#errors').show();
	if(errorType==0){
		$('#errors').removeClass('panel-danger');
		$('#errors').addClass('panel-info');
		$('#errors_type').html("Information: Une erreur non bloquante est intervenue lors du processus de d&eacute;ploiement ");
	}	else	{
		$('#errors').removeClass('panel-info');
		$('#errors_type').html("Erreur");
		$('#errors').addClass('panel-danger');
		$('#errors_type').html("Erreur: Une erreur bloquante est intervenue lors du processus de d&eacute;ploiement ");
		$('#workflow_actions').html('');
	}
}

function longAction( ){
	var resultJsp='jsp/admin/plugins/deployment/CommandResultJSON.jsp';
	$.getJSON( resultJsp, function(data){	statusCallback(data); });
}

/*
 * Delay en ms
 */
function refreshCommandResult( resultJsp ){
   _refresh = setTimeout( longAction, 2000 );
}

function statusCallback( json ){
	if ( json == null || json == "" )	{
		setTimeout( longAction, 2000 );
	} else	{
		if(json.status!=1){
			setErrors(json.error,json.error_type);
		}
		setResultLog( json.log );
		setResultInformations( json.result );
		if ( json.running ){
			/* still running */
			setTimeout( longAction, 2000 );
		} else {
			/* ended */
			stopAction( _refresh, json );
		}
	}
}

function initComponantsEnvironment(){
	$("#code_environment").selectedIndex = 0;
	// see selectbox.js
	removeAllOptions( document.getElementById("code_server_application_instance_tomcat") );
	removeAllOptions( document.getElementById("code_server_application_instance_mysql") );
}

function refreshComponantsTomcat(){
	var selectTomcat = document.getElementById("code_server_application_instance_tomcat");
	// first, remove all tomcat
	removeAllOptions( selectTomcat );
	var selectEnvironment = document.getElementById("code_environment");
	var selectedEnvironment = selectEnvironment.options[selectEnvironment.selectedIndex].value;
	if ( selectedEnvironment != null && selectedEnvironment != "" ){
	   // add options Tomcat
		var itemListTomcat = itemsMapServerTomcat[selectedEnvironment];
		// add empty value
		addOption( selectTomcat,"","",true );
		for ( var i = 0; i < itemListTomcat.length; i++ ){
			var item = itemListTomcat[i];
			addOption(selectTomcat,item["name"],item["code"],false);
		}
                if ( selectedEnvironment == 'R7')
                var mvnProfile = '';
                switch ( selectedEnvironment )
                {
                    case 'v1.r7':
                        mvnProfile = 'rec';
                        break;
                    case 'v1.pr':
                        mvnProfile = 'prod';
                        break;
                    case 'v1.dv':
                        mvnProfile = 'dev';
                        break;
                    case 'v1.pp':
                        mvnProfile = 'preprod';
                        break;
                    case 'v1.fo':
                        mvnProfile = 'formation';
                        break;
                    case 'ville.fo':
                        mvnProfile = 'formation';
                        break;
                }
                $('#maven_custom_goal').val( "-P "+ mvnProfile +" -U " + $('#base_maven_custom_goal').val( ) );
	}
}

function refreshComponantsSql(){
	var selectSql = document.getElementById("code_server_application_instance_sql");
	// first, remove all mysql
	removeAllOptions( selectSql );
	var selectEnvironment = document.getElementById("code_environment");
	var selectedEnvironment = selectEnvironment.options[selectEnvironment.selectedIndex].value;
	if ( selectedEnvironment != null && selectedEnvironment != "" ){
	  	// add options Mysql
		var itemListSql = itemsMapServerSql[selectedEnvironment];
		// add empty value
		addOption( selectSql,"","",true );
		for ( var i = 0; i < itemListSql.length; i++ ) {
			var item = itemListSql[i];
			addOption(selectSql,item["name"],item["code"],false);
		}
	}
}

function refreshComponantsDatabase(){
	var selectEnvironment = document.getElementById("code_environment");
	var selectSql = document.getElementById("code_server_application_instance_sql");
	var selectDatabase = document.getElementById("code_database");
	// first, remove all mysql
	removeAllOptions( selectDatabase );
	var selectedEnvironment = selectEnvironment.options[selectEnvironment.selectedIndex].value;
	var selectedSql = selectSql.options[selectSql.selectedIndex].value;
	if ( selectedEnvironment != null && selectedEnvironment != "" && selectedSql != null && selectedSql != "" )
	{
	   // add options Mysql
		var itemListDatabase = itemsMapDatabase[selectedEnvironment+"."+selectedSql];
		// add empty value
		addOption( selectDatabase,"","",true );
		for ( var i = 0; i < itemListDatabase.length; i++ )
		{
			var item = itemListDatabase[i];
			addOption(selectDatabase,item["name"],item["code"],false);
		}
	}
}

function runWorkflowAction( idAction ){
	var resultJsp='jsp/admin/plugins/deployment/DoProcessActionJSON.jsp?id_action='+idAction;
	$('#myModal').modal('show');
	$.getJSON( resultJsp, function(data){	statusCallbackWorkflowAction(data);	});
	refreshCommandResult();
}

function addEvent(){
  $('#workflow_actions form').submit( function(){
  	var idAction = $(this).children().first().val();
  	runWorkflowAction( idAction);
		return false;
	});
  $('.form_run_action_server').submit( function(){
  	runActionServer( $(this) );
  	return false;
	});
}

function statusCallbackWorkflowAction( json ){
	if ( json == null || json == "" ){
	}	else {
		$('#myModal').modal('hide');
		if(json.jsp_form_display!=null && json.jsp_form_display!= "" ){
			displayTaskForm(json);

		}	else {
			replaceWorkflowActions(json);
			replaceWorkflowState(json);

		}
	}
}

function notifyAction(json){
if ( json.form_error == '' )	{
	$.notify({
				title: '<strong>Action</strong>',
				message: json.state
			},{
				type: 'success'
			},{
				offset: 50
			},{
				animate: {
					enter: 'animated fadeInRight',
					exit: 'animated fadeOutRight'
				}
			});
}	else	{
	$.notify({
		title: '<strong>Erreur</strong>',
		message: json.state +' : '+ json.form_error
		},{
			type: 'danger'
	});
 }
}

function stopAction( refresh, json ){
	logArea = document.getElementById( "pre_log" );
	clearTimeout( refresh );
	//document.getElementById("div-waiting").style.display="none";
	if ( json.status == 0 )	{	}	else	{ }
	$('#myModal').modal('hide');
}



function replaceWorkflowActions(json){
	if(json.action_list!=null){
		var newActions='';
		var action;
		for (val in json.action_list){
			action=json.action_list[val]
			newActions+='<form class="form-horizontal" method="post"  name="workflow_action_'+action.id+'" id="workflow_action_'+action.id+'" action="jsp/admin/plugins/deployment/DoProcessAction.jsp">';
			newActions+='<input type="hidden" name="id_action" id="id_action" value="'+action.id+'">';
			newActions+='<div class="form-group">';
			newActions+='<div class="col-xs-12 col-sm-12 col-md">'
			newActions+='<button class="btn btn-primary btn-lg btn-flat" type="submit"><i class="fa fa-play"></i> '+ action.name +'</button>';
			newActions+='<a class="btn btn-default btn-lg btn-flat" href="jsp/admin/plugins/deployment/ManageApplication.jsp?plugin_name=deployment" title="#i18n{portal.util.labelCancel}"><i class="fa fa-remove"></i> Annuler </a></div>';
			newActions+='</div>';
			newActions+='<p class="help-block">'+action.description+'</p>';
			newActions+='</form>';
		}
		$('#workflow_actions').html( newActions );
		//Add Event on new action
		$('#workflow_actions form').submit( function(){
    	var idAction = $(this).children().first().val();
    	runWorkflowAction( idAction);
			return false;
		});
	}
}

function replaceWorkflowState(json){
	if(json.state!=null && json.state!= "" ){
		var info= json.state + '<p><a class="btn btn-default btn-lg btn-flat" href="jsp/admin/plugins/deployment/ManageApplication.jsp?plugin_name=deployment" title=""><i class="fa fa-remove"></i> Retour </a></p>';
		$('#workflow_state').html( info );
	}
}

function displayTaskForm(json){
	$('#workflow_actions').html("");
	$.ajax({
		  url: json.jsp_form_display,
		  success:function ( data ) {
			$('#workflow_task_form').html(data);
			$('#workflow_task_form').show();
		}
	});
}

function saveTasksForm( form ){
	$('#myModal').modal('show') ;
	$.ajax({
      url: 'jsp/admin/plugins/deployment/DoSaveTasksFormJSON.jsp',
      type: $(form).attr('method'),
      data: $(form).serialize(),
      dataType: 'json',
      success: function(json) {
		statusCallbackTasksForm(json);
    }
  });
}

function statusCallbackTasksForm( json ){
$('#myModal').modal('hide');
	if ( json == null || json == "" ){
		return;
	}
	if(json.form_error!=null && json.form_error!= "" ){
		$('#task_form_error').html(json.form_error);
	}	else	{
		$('#myModal').modal('hide');
		$('#workflow_task_form').hide();
		replaceWorkflowActions(json);
		replaceWorkflowState(json);
	}
}

/**************Servers actions*********************/

function runActionServer(form ){
	//initModalBody();
	if ($('#myModal').hasClass('in')==false){
		$('#myModal').modal('show') ;
	}
	$('#myModal div.action_wait').show();
	$('#myModal div.modal-body div.modal-content').html("");
	$('#myModal div.modal-body div.error_run_action_server span').html("");
	$('#myModal div.modal-body div.error_run_action_server').hide();
	$.ajax({
    url: 'jsp/admin/plugins/deployment/DoRunActionServerJSON.jsp',
    type: $(form).attr('method'),
    data: $(form).serialize(),
    dataType: 'json',
    success: function(json) {
  	  statusCallbackRunActionServer(json);
    }
  });
}

function statusCallbackRunActionServer( json ){
	if ( json == null || json == "" ){
	}	else {
		if(json.jsp_form_display!=null && json.jsp_form_display!= "" ){
			$('#myModal div.action_wait').hide();
			displayFormActionServer(json);
		}	else if(json.status==0)	{
			$('#myModal div.action_wait').hide();
			$('#myModal div.error_run_action_server span').html(json.error);
			$('#myModal div.modal-body div.error_run_action_server').show();
		}	else	{
			//setResultActionServerLog( json.log );
			setResultActionServer( json.result );
			replaceServerActions(json);
			replaceServerStatus(json)
		}
	}
}

function displayFormActionServer(json){
	$.ajax({
	  url: json.jsp_form_display,
	  success:function ( data ) {
			$('.modal-body div.modal-content').html(data);
			$('#myModal').modal('show');
		}
	});
}

function setResultActionServerLog( log ){
	$('#action_server_log').html(log);
	$('#action_server_result').toggle();
}

function setResultActionServer( resultInformations ){
	if(resultInformations.dump_file_url!=null && resultInformations.dump_file_url!= ""){
		$('#myModal div.action_wait').hide();
		$('#myModal div.modal-body div.modal-content').html(" <a class='btn btn-primary btn-flat btn-block' href='"+resultInformations.dump_file_url+"' title='Télécharger le dump' > <i class='glyphicon glyphicon-download'></i> Télécharger le dump</a>");
	} else {
		$('#myModal').modal('hide');
	}
}

function replaceServerActions(json){
	if(json.action_list!=null)
	{	var newActions='';
		var action, btnType;
		for (val in json.action_list){
			action=json.action_list[val];
			if ( action.action_code=='@SHUTDOWN'){
				btnType='danger';
			} else if( action.action_code=='@STARTUP'){
				btnType='success';
			} else {
				btnType='primary';
			}
			newActions+='<form action="jsp/admin/plugins/deployment/DoRunActionServer.jsp?plugin_name=deployment" method="post" name="form_action_server"  class="form-inline pull-left spaced form_run_action_server">';
			newActions+='<input name="plugin_name" value="deployment" type="hidden" >';
			newActions+='<input name="id_application" value="'+json.id_application+'" type="hidden" >';
			newActions+='<input name="action_code" value="'+action.action_code+'" type="hidden" />'
			newActions+='<input name="code_environment" value="'+json.code_environment+'" type="hidden" />';
			newActions+='<input name="code_server_application_instance" value="'+json.code_server_application_instance+'" type="hidden" >';
			newActions+='<input name="server_application_type" value="'+json.server_application_type+'" type="hidden" >';
			newActions+=' <button class="btn btn-' + btnType + ' btn-flat" type="submit" >';
			newActions+='<i class="'+action.icon_css_class+'"></i> '+action.name;
			newActions+='</button>';
			newActions+='</form>';
		}
		$('#actions_'+json.code_environment.replace(".", "_")+'_'+json.code_server_application_instance+'_'+json.server_application_type).html(newActions);
		$('#actions_'+json.code_environment.replace(".", "_")+'_'+json.code_server_application_instance+'_'+json.server_application_type+' form').submit( function(){
			runActionServer( $(this) );
    	return false;
		});
	}
}

function replaceServerStatus(json) {
	if( json.server_status!=null ){
		var newStatus='';
		if( json.server_status==1 ){
			newStatus+=' <i class="fa fa-check-circle-o text-success" title="Activé"></i> ';
		} else {
			newStatus+=' <i class="fa fa-times-circle-o text-danger" title="Désactivé"></i> ';
		}
	  $('#status_'+json.code_environment.replace(".", "_")+'_'+json.code_server_application_instance+'_'+json.server_application_type).html(newStatus);
	}
}

function initModalBody() {
	$('.modal-body').html('<div class="box" style=""><div class="overlay"> <i class="fa fa-refresh fa-spin"></i></div></div><div class="center-block">Une action est en cours, veuillez patienter quelques instants</div><div class="alert alert-danger hide" id="error_run_action_server" role="alert" ><span></span></div>');
}

function displayChangeMavenProfile(idApplication,codeEnvironment,codeServerApplicationInstance,serverApplicationType,mvnProfile){
	
    $('#myModal').modal('show') ;
	$('#myModal div.action_wait').hide();
	
	$('#myModal div.modal-body div.modal-content').html('<form action="jsp/admin/plugins/deployment/DoModifyMavenProfil.jsp?plugin_name=deployment" method="post" name="form_modify_profil">'
														    +'<input name="plugin_name" value="deployment" type="hidden" >'
														    +'<input name="id_application" value="'+idApplication+'" type="hidden" >'
														    +'<input name="code_environment" value="'+codeEnvironment+'" type="hidden" />'
														    +'<input name="code_server_application_instance" value="'+codeServerApplicationInstance+'" type="hidden" >'
														    +'<input name="server_application_type" value="'+serverApplicationType+'" type="hidden" >'
														    +'<div class="form-group">'
															+'<label for="maven_profil">Nom du profile Maven</label>'
															+'<div>'
															+'<input type="text" name="maven_profil" class="form-control" value="'+mvnProfile+'">'
															+'</div>'
															+'</div>'
															+'<button class="btn btn-primary btn-falt" type="submit">'
															+'<span class="fa fa-check"></span> Modifier le profil Maven'
															+'</button>'
														    +'</form>');
	$('#myModal div.modal-body div.error_run_action_server span').html("");
	$('#myModal div.modal-body div.error_run_action_server').hide();

}

$(function() {
	
	$('.modify_maven_profile').click(function(e){
		var idApplication = $(this).data("id-application");
		var codeEnvironment= $(this).data("code-environment");
		var codeServerApplicationInstance= $(this).data("code-server-application-instance");
		var serverApplicationType= $(this).data("server-application-type");
		var mvnProfile= $(this).data("mvn-profile");
		
		displayChangeMavenProfile(idApplication,codeEnvironment,codeServerApplicationInstance,serverApplicationType,mvnProfile);
	});
});

/*****************************************************/
