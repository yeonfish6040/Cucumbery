package com.jho5245.cucumbery.util.itemeditor;

import com.jho5245.cucumbery.Cucumbery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;

public class JsonTranslatableComponent extends JsonComponent
{
	public List<JsonComponent> arguments;

	public JsonTranslatableComponent(String key)
	{
		this.text = key;
	}

	public JsonTranslatableComponent(String key, List<JsonComponent> arguments)
	{
		super();
		this.text = key;
		this.arguments = arguments;
	}

	@Override
	@SuppressWarnings("all")
	public String toString()
	{
		try
		{
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonProperty.toString());
			jsonObject.put("translate", text);
			if (arguments != null && arguments.size() > 0)
			{
				JSONArray array = new JSONArray();
				array.addAll(arguments);
				jsonObject.put("with", array);
			}
			return jsonObject.toString();
		}
		catch (ParseException e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		return null;
	}
}
