package vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



public class ParagrafoVO {
	 public ParagrafoVO() {
			super();
			// TODO Auto-generated constructor stub
		}

	    private Long id;

	 
	    private Integer numero;

	
	    private String texto;
	    
	    private List<AnexoVO> anexos; 
	
	    private String resumo;

	   
	    private List<PerguntaVO> perguntas = new ArrayList<>();

	

	   
	    private Long paginaId;


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public Integer getNumero() {
			return numero;
		}


		public void setNumero(Integer numero) {
			this.numero = numero;
		}


		public String getTexto() {
			return texto;
		}


		public void setTexto(String texto) {
			this.texto = texto;
		}


		public String getResumo() {
			return resumo;
		}


		public void setResumo(String resumo) {
			this.resumo = resumo;
		}


		public List<PerguntaVO> getPerguntas() {
			return perguntas;
		}


		public void setPerguntas(List<PerguntaVO> perguntas) {
			this.perguntas = perguntas;
		}
		public List<AnexoVO> getAnexos() {
			return anexos;
		}


		public void setAnexos(List<AnexoVO> anexos) {
			this.anexos = anexos;
		}


		public ParagrafoVO(Long id, Integer numero, String texto, String resumo, List<PerguntaVO> perguntas,
				Long paginaId) {
			super();
			this.id = id;
			this.numero = numero;
			this.texto = texto;
			this.resumo = resumo;
			this.perguntas = perguntas;
			this.paginaId = paginaId;
		}


		public Long getPaginaId() {
			return paginaId;
		}


		public void setPaginaId(Long paginaId) {
			this.paginaId = paginaId;
		}
		 // prettier-ignore
	    @Override
	    public String toString() {
	        return "Paragrafo{" +
	            "id=" + getId() +
	            ", numero=" + getNumero() +
	            ", texto='" + getTexto() + "'" +
	            ", resumo='" + getResumo() + "'" +
	            "}";
	    }


		
}
