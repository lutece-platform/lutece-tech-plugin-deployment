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