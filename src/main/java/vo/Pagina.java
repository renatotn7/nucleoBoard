package vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A Pagina.
 */

public class Pagina implements Serializable {
	
    public Pagina() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

 
    private Long id;

 
    private Integer numero;

    private String texto;

    private String planoDeAula;

    private byte[] imagem;

    private String imagemContentType;

  
    private List<ParagrafoVO> paragrafos = new ArrayList<>();

    
    
    private Long livroId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pagina id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Pagina numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTexto() {
        return this.texto;
    }

    public Pagina texto(String texto) {
        this.setTexto(texto);
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getPlanoDeAula() {
        return this.planoDeAula;
    }

    public Pagina planoDeAula(String planoDeAula) {
        this.setPlanoDeAula(planoDeAula);
        return this;
    }

    public void setPlanoDeAula(String planoDeAula) {
        this.planoDeAula = planoDeAula;
    }

    public byte[] getImagem() {
        return this.imagem;
    }

    public Pagina imagem(byte[] imagem) {
        this.setImagem(imagem);
        return this;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getImagemContentType() {
        return this.imagemContentType;
    }

    public Pagina imagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
        return this;
    }

    public void setImagemContentType(String imagemContentType) {
        this.imagemContentType = imagemContentType;
    }

    public List<ParagrafoVO> getParagrafos() {
        return this.paragrafos;
    }

    public void setParagrafos(List<ParagrafoVO> paragrafos) {
        if (this.paragrafos != null) {
            this.paragrafos.forEach(i -> i.setPaginaId(this.getId()));
        }
        if (paragrafos != null) {
            paragrafos.forEach(i -> i.setPaginaId(this.getId()));
        }
        this.paragrafos = paragrafos;
    }

    public Pagina paragrafos(List<ParagrafoVO> paragrafos) {
        this.setParagrafos(paragrafos);
        return this;
    }

    public Pagina addParagrafo(ParagrafoVO paragrafo) {
        this.paragrafos.add(paragrafo);
        paragrafo.setPaginaId(this.getId());
        return this;
    }

    public Pagina removeParagrafo(ParagrafoVO paragrafo) {
        this.paragrafos.remove(paragrafo);
        return this;
    }

 

    public Pagina(Long id, Integer numero, String texto, String planoDeAula, byte[] imagem, String imagemContentType,
    		List<ParagrafoVO> paragrafos, Long livroId) {
		super();
		this.id = id;
		this.numero = numero;
		this.texto = texto;
		this.planoDeAula = planoDeAula;
		this.imagem = imagem;
		this.imagemContentType = imagemContentType;
		this.paragrafos = paragrafos;
		this.livroId = livroId;
	}

	public Long getLivroId() {
        return this.livroId;
    }

    public void setLivroId(Long livro) {
        this.livroId = livro;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pagina)) {
            return false;
        }
        return id != null && id.equals(((Pagina) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pagina{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", texto='" + getTexto() + "'" +
            ", planoDeAula='" + getPlanoDeAula() + "'" +
            ", imagem='" + getImagem() + "'" +
            ", imagemContentType='" + getImagemContentType() + "'" +
            "}";
    }
}
