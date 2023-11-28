package com.jho5245.cucumbery.util.itemeditor;

import com.jho5245.cucumbery.Cucumbery;
import org.json.simple.JSONObject;

public class JsonProperty implements Cloneable
{
	public String color;

	public Boolean italic;

	public Boolean bold;

	public Boolean underlined;

	public Boolean strikethrough;

	public Boolean obfuscated;

	public JsonProperty()
	{

	}

	public JsonProperty(String color)
	{
		this(color, null, null, null, null, null);
	}

	public JsonProperty(String color, Boolean italic, Boolean bold, Boolean underlined, Boolean strikethrough, Boolean obfuscated)
	{
		this.color = color;
		this.italic = italic;
		this.bold = bold;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
	}

	@Override
	@SuppressWarnings("all")
	public String toString()
	{
		JSONObject jsonObject = new JSONObject();
		if (color != null)
		{
			jsonObject.put("color", color);
			jsonObject.put("italic", false);
		}
		if (italic != null)
		{
			jsonObject.put("italic", italic);
		}
		if (bold != null)
		{
			jsonObject.put("bold", bold);
		}
		if (underlined != null)
		{
			jsonObject.put("underlined", underlined);
		}
		if (strikethrough != null)
		{
			jsonObject.put("strikethrough", strikethrough);
		}
		if (obfuscated != null)
		{
			jsonObject.put("obfuscated", obfuscated);
		}
		return jsonObject.toString();
	}

	@Override
	public JsonProperty clone()
	{
		try
		{
			return (JsonProperty) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			Cucumbery.getPlugin().getLogger().warning(e.getMessage());
		}
		return null;
	}
}
