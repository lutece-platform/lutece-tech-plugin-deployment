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
		/* error */
		/*alert("erreur de javascript");*/
		return;
	}
	setResultLog( json.log );
	if ( json.running )
	{
		/* still running */
		setTimeout( longAction, 2000 );
	}
	else
	{
		/* ended */
		//stopAction( _refresh, json );
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
	
	$.getJSON( "jsp/admin/plugins/deployment/DoProcessActionJSON.jsp?id_action="+idAction, function(data)
			{
			statusCallbackWorkflowAction(data);
			});
	refreshCommandResult();
}


function statusCallbackWorkflowAction( json )
{
	if ( json == null || json == "" )
	{
		/* error */
		/*alert("erreur de javascript");*/
		return;
	}
	if(json.jsp_forced_redirect!=null && json.jsp_forced_redirect!= "" )
	{
		
		window.location =json.jsp_forced_redirect;
		
	}
	else
	{
		
		replaceWorkflowActions(json);
		replaceWorkflowState(json);
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
	
	
	
}


function replaceWorkflowState(json)
{
	
	
	
}



