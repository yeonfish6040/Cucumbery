package com.jho5245.cucumbery.util.itemeditor;

import com.jho5245.cucumbery.Cucumbery;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonComponent
{
	public String text;

	public JsonProperty jsonProperty;

	public JsonComponent()
	{
		this("", new JsonProperty());
	}

	public JsonComponent(String text)
	{
		this(text, new JsonProperty());
	}

	public JsonComponent(String text, JsonProperty jsonProperty)
	{
		this.text = text;
		this.jsonProperty = jsonProperty;
	}

	@Override
	@SuppressWarnings("all")
	public String toString()
	{
		try
		{
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonProperty.toString());
			jsonObject.put("text", text);
			return jsonObject.toString();
		}
		catch (ParseException e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		return null;
	}
}
