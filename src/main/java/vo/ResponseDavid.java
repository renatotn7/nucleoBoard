package vo;

import java.util.List;

public class ResponseDavid {
	public String id;
	public String object;
	public Long created;
	public String model;
	public List<Choices> choices;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public Long getCreated() {
		return created;
	}
	public void setCreated(Long created) {
		this.created = created;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public List<Choices> getChoices() {
		return choices;
	}
	public void setChoices(List<Choices> choices) {
		this.choices = choices;
	}
}
