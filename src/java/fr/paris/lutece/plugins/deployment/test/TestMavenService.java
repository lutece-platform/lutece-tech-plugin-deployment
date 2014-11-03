/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.deployment.test;

public class TestMavenService
{
    final private String strTagTest = "site-test-release-1.0.0";

    //	@Test
    //	public void mvnSiteAssemmbly()
    //	{
    //		
    //		
    //		User user=new User();
    //		PropertiesService.getInstance().setRealPath("C:\\java\\ws\\ws-intraparis\\site-deploiement\\src\\main\\webapp");
    //		try {
    //			PropertiesService.getInstance().loadFile(PropertiesService.getInstance().PATH_CONFIG_FILE);
    //		} catch (FileNotFoundException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		} catch (IOException e) {
    //			// TODO Auto-generated catch block
    //			e.printStackTrace();
    //		}
    //		
    //		
    //		Environment environment=new Environment();
    //		environment.setCode("RECETTE");
    //		environment.setMavenProfile("recette");
    //		MavenService.getInstance().mvnSiteAssembly(strTagTest, environment,user);
    //		
    //	}

    //	@Test
    //	public void webService()
    //	{
    //		
    //		String jsonEnvironement="{\"is_ok\": true, \"accepted_commands\": [], \"information_message\": \"\", \"next\": {\"aeras\": [\"DMZ\", \"VILLE\"]}, \"execution\": {\"steps\": [], \"has_been_executed\": false}, \"datas\": []}";
    //		JSONObject jo=(JSONObject) JSONSerializer.toJSON( jsonEnvironement);  
    //		JSONArray jsonArray=jo.getJSONObject("next").getJSONArray("aeras");
    //		Iterator iterator=jsonArray.iterator();
    //		while(iterator.hasNext())
    //		{
    //			
    //			System.out.println(((String)iterator.next()));
    //		}
    //		
    //	}
    //	
}
