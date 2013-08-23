package org.squadra.atenea.actions;

public class Click {

	private String typeOfClick;
	private String pathOfIcon;
	
	
	public Click(String typeOfClick, String pathOfIcon) {
		super();
		this.typeOfClick = typeOfClick;
		this.pathOfIcon = pathOfIcon;
	}
	
	
	public String getTypeOfClick() {
		return typeOfClick;
	}
	public void setTypeOfClick(String typeOfClick) {
		this.typeOfClick = typeOfClick;
	}
	public String getPathOfIcon() {
		return pathOfIcon;
	}
	public void setPathOfIcon(String pathOfIcon) {
		this.pathOfIcon = pathOfIcon;
	}
	
	
}
