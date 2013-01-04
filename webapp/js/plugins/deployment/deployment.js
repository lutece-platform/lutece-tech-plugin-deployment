var _refresh;
var delay = 2000; // might be useless...


function setResultLog( log )
{
	logArea = document.getElementById( "pre_log" );
	logArea.value = log;
	logArea.scrollTop = logArea.scrollHeight; 
}



function longAction( ) 
{
	
	var resultJsp='jsp/admin/plugins/deployment/CommandResultJSON.jsp';
	$.getJSON( resultJsp, function(data)
			{
				statusCallback(data);
			});
}

/*
 * Delay en ms
 */
function refreshCommandResult( resultJsp )
{
	
   _refresh = setTimeout( longAction, 2000 ); 
}

function statusCallback( json )
{	
	
	if ( json == null || json == "" )
	{
		setTimeout( longAction, 2000 );
	}
	else
	{
		setResultLog( json.log );
		if ( json.running )
		{
			/* still running */
			setTimeout( longAction, 2000 );
		}
		else
		{
			/* ended */
			stopAction( _refresh, json );
		}
	}
}


function initComponants()
{
	// clear category selection
	$("#code_category").selectedIndex = 0;
	// see selectbox.js
	removeAllOptions( document.getElementById("site") );
}

function refreshComponants()
{
	var select = document.getElementById("site"); 
	// first, remove all
	removeAllOptions( select );
	
	var selectCategory = document.getElementById("code_category");
	
	// add options
	var selectedCategory = selectCategory.options[selectCategory.selectedIndex].value;
	if ( selectedCategory != null && selectedCategory != "" )
	{
		var itemList = itemsMap[selectedCategory];
		// add empty value
		addOption( select,"","",true );
		for ( var i = 0; i < itemList.length; i++ )
		{
			var item = itemList[i];
			addOption(select,item["name"],item["code"],false);
		}
	}
}



function initComponantsEnvironment()
{
	// clear category selection
	$("#code_environment").selectedIndex = 0;
	// see selectbox.js
	removeAllOptions( document.getElementById("code_server_application_instance") );
}

function refreshComponantsEnvironment()
{
	var select = document.getElementById("code_server_application_instance"); 
	// first, remove all
	removeAllOptions( select );
	
	var selectEnvironment = document.getElementById("code_environment");
	
	// add options
	var selectedEnvironment = selectEnvironment.options[selectEnvironment.selectedIndex].value;
	if ( selectedEnvironment != null && selectedEnvironment != "" )
	{
		var itemList = itemsMap[selectedEnvironment];
		// add empty value
		addOption( select,"","",true );
		for ( var i = 0; i < itemList.length; i++ )
		{
			var item = itemList[i];
			addOption(select,item["name"],item["code"],false);
		}
	}
}


function runWorkflowAction( idAction ) 
{
	var resultJsp='jsp/admin/plugins/deployment/DoProcessActionJSON.jsp?id_action='+idAction;
	$.getJSON( resultJsp, function(data)
			{
		statusCallbackWorkflowAction(data);
			});
	refreshCommandResult();
}

function addEvent() {
	  
    $('#workflow_actions form').submit( function(){
        
    	var idAction = $(this).children().first().val();
    	runWorkflowAction( idAction);
		  
			return false;
	});

    $('#task_form').submit( function(){
    	saveTasksForm( $(this) );
    	return false;
	});
}


function statusCallbackWorkflowAction( json )
{
	
	if ( json == null || json == "" )
	{
	
	}
	else
	{
	
		if(json.jsp_form_display!=null && json.jsp_form_display!= "" )
		{
			
			displayTaskForm(json);
			
		}
		else
		{
			
			replaceWorkflowActions(json);
			replaceWorkflowState(json);
			
		}
		addEvent();
	}
	
}


function stopAction( refresh, json )
{
	logArea = document.getElementById( "pre_log" );
	clearTimeout( refresh );
	//document.getElementById("div-waiting").style.display="none";
	if ( json.status == 0 )
	{
		
	}
	else
	{
		
	}
}


function replaceWorkflowActions(json)
{
	
	if(json.action_list!=null)
	{
		var newActions='';
		var action;
		for (val in json.action_list){ 

			action=json.action_list[val]
			newActions+='<form class="form-horizontal" method="post"  name="workflow_action_'+action.id+'" id="workflow_action_'+action.id+'" action="jsp/admin/plugins/deployment/DoProcessAction.jsp">';
			newActions+='<input type="hidden" name="id_action" id="id_action" value="'+action.id+'" />';
			newActions+='<div class="form-actions">';
			newActions+='<button class="btn btn-primary" type="submit" >'+action.name+'</button>';
			newActions+='</div>';
			newActions+='<p class="help-block">'+action.description+'</p>';
			newActions+='</form>';	

			}		

									
		$('#workflow_actions').html(newActions);
        		

	}
	
}


function replaceWorkflowState(json)
{
	if(json.state!=null && json.state!= "" )
	{
		
		$('#workflow_state').html(json.state);
		
	}
	
	
}


function displayTaskForm(json)
{
	
	$.ajax({
		  url: json.jsp_form_display,
		  success:function ( data ) {
			$('#workflow_task_form').html(data);
			$('#workflow_task_form').show();
		}
	});
	
}





function saveTasksForm(form ) 
{
	
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



function statusCallbackTasksForm( json )
{
   
	if ( json == null || json == "" )
	{
		/* error */
		/*alert("erreur de javascript");*/
		return;
	}
	if(json.form_error!=null && json.form_error!= "" )
	{
		$('#task_form_error').html(json.form_error);
		addEvent();
	}
	else
	{
		$('#workflow_task_form').hide();
		replaceWorkflowActions(json);
		replaceWorkflowState(json);
		addEvent();
	}
	
	
}




