package vo;


public class RespostaVO {
  
    private Long id;


    private String texto;


	public RespostaVO(Long id, String texto) {
		super();
		this.id = id;
		this.texto = texto;
	}
	public RespostaVO() {
		
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}
}
