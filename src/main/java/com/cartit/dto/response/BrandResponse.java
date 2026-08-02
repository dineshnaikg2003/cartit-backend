package com.cartit.dto.response;

public class BrandResponse {

    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private Boolean active;

    public BrandResponse(Long id,
                         String name,
                         String description,
                         String logoUrl,
                         Boolean active) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.active = active;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}