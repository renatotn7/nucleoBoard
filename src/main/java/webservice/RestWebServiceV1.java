package webservice;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import main.StringWorks;
import service.PageBoardService;

import vo.Pagina;
import vo.ParagrafoVO;
import vo.RespostaServerVO;
import vo.TextoIn;

@RestController
public class RestWebServiceV1 {
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	@PostMapping("/textoEmTopicos")
	public ParagrafoVO textoEmTopicos(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.textoEmTopicos(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/separaBlocos")
	public List<String> separaEmBlocos(@RequestBody TextoIn textoin) {
		String texto = textoin.getTexto();

		List<String> blocosPlus = new ArrayList<String>();
		List<String> blocosReorganizados = new ArrayList<String>();
		List<String> blocosFinal = new ArrayList<String>();

		String[] linhas;
		for (int a = 0; a < 2; a++) {
			if (blocosFinal.size() == 0) {
				linhas = StringWorks.paragrafoBrutoParaLinha(texto).split("\\r\\n");
			} else {
				linhas = new String[blocosFinal.size()];
				for(int i=0;i<linhas.length;i++) {
					linhas[i]=blocosFinal.get(i);
				}
			//	linhas = blocosFinal.toArray(String[]);
			}
			blocosPlus = new ArrayList<String>();
			blocosReorganizados = new ArrayList<String>();
			blocosFinal = new ArrayList<String>();

			List<Integer> indices = new ArrayList<Integer>();
			for (int i = 0; i < linhas.length; i++) {
				String linha = linhas[i];
				if (linha.length() < 340 && !(indices.contains(i))) {// ? nao tenho certeza que tenha que ser assim no
																		// indices
					if (linha.split(" ").length < 5 && (i + 1) < linhas.length && !linha.matches("\\.|\\,|\\:|\\;")) {
						blocosPlus.add(linha + ": " + linhas[i + 1]);
						indices.add(i);
						indices.add(i + 1);
						i++;
						continue;
					}
					if ((i + 1) < linhas.length && (linhas[i + 1].length() + linha.length()) < 900) {

						blocosPlus.add(linha + " " + linhas[i + 1]);
						indices.add(i);
						indices.add(i + 1);
						i++;
						continue;
					} else {
						blocosPlus.add(linha);
					}

				} else {
					// tenho que checar esse caso tb

					blocosPlus.add(linha);
				}
			}
			indices = new ArrayList<Integer>();
			Deque<String> priority = new ArrayDeque<String>();
			for (int i = blocosPlus.size() - 1; i > -1; i--) {
				String linha = blocosPlus.get(i);

				if (linha.length() < 340 && (!indices.contains(i))) {// ? nao tenho certeza que tenha que ser assim no
																		// indices

					if ((i - 1) > -1 && (blocosPlus.get(i - 1).length() + linha.length()) < 740) {
						// tenho que checar se essa união ja não aconteceu posso precisar de um array de
						// indices
						// caso tudo bem
						indices.add(i);
						indices.add(i - 1);

						priority.push(blocosPlus.get(i - 1) + " " + linha);
						// blocosFinal.add(blocosPlus.get(i-1) + " " + linha);
						i--;
					} else {
						priority.push(linha);
					}

				} else {
					// tenho que checar esse caso tb

					priority.push(linha);
				}
			}
			while (!priority.isEmpty()) {
				blocosReorganizados.add(priority.pop());
			}

			for (int i = 0; i < blocosReorganizados.size(); i++) {
				String bloco = blocosReorganizados.get(i).trim();
				int pos = -1;
				if (bloco.length() > 740) {
					int h=740;
					for (int j = 740; j < bloco.length(); j++,h--) {
						if (bloco.charAt(j) == '.') {
							String blocoA = bloco.substring(0, j + 1);
							String blocoB = bloco.substring(j + 1);
							if ( blocoA.length() >= 340 && blocoB.length() >= 340) {
								
								blocosFinal.add(blocoA.trim());
								blocosFinal.add(blocoB.trim());
								break;
							}else {
								blocosFinal.add(bloco);
								break;
							}
//bgcc_orquestradorconteudodp 
						}
						if (bloco.charAt(h) == '.') {
							String blocoA = bloco.substring(0, h + 1).trim();
							String blocoB = bloco.substring(h + 1).trim();
							if ( blocoA.length() >= 340 && blocoB.length() >= 340) {
								
								blocosFinal.add(blocoA.trim());
								blocosFinal.add(blocoB.trim());
								break;
							}else {
								blocosFinal.add(bloco);
								break;
							}

						}
					}
				} else {
					blocosFinal.add(bloco);
				}
			}
		}
		return blocosFinal;
	   // return repository.findAll();
	  }
	
	
	@PostMapping("/expliqueOSeguinteTexto")
	public ParagrafoVO expliqueOSeguinteTexto(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.expliqueOSeguinteTexto(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/expliqueOSeguinteTextoComTitulos")
	public ParagrafoVO expliqueOSeguinteTextoComTitulos(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.expliqueOSeguinteTextoComTitulos(textoin.getTexto());
	   // return repository.findAll();
	  }
	
	@PostMapping("/sequenciaDeAcontecimentos")
	public ParagrafoVO sequenciaDeAcontecimentos(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.sequenciaDeAcontecimentos(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/deixarAtraenteJornalistico")
	public ParagrafoVO deixarAtraenteJornalistico(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.deixarAtraenteJornalistico(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/tituloConvidativo")
	public ParagrafoVO tituloConvidativo(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.tituloConvidativo(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/criatabeladefatos")
	public ParagrafoVO criatabeladefatos(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.criatabeladefatos(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/extraiTagsDoTexto")
	public ParagrafoVO extraiTagsDoTexto(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.extraiTagsDoTexto(textoin.getTexto());
	   // return repository.findAll();
	  }
	
	@PostMapping("/geraTextoSimplificado")
	public ParagrafoVO getTextoSimplificado(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.textoSimplificado(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/geraPerguntasMultiplaEscolha")
	public ParagrafoVO getPerguntasMultEsc(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.perguntasMultiplaEscolha(textoin.getTexto());
	   // return repository.findAll();
	  }
	@PostMapping("/geraPerguntasDiscursivas")
	public ParagrafoVO getPerguntasDiscursivas(@RequestBody TextoIn textoin) {
		PageBoardService pbs = new PageBoardService();
		return pbs.perguntasDiscursivas(textoin.getTexto());
	   // return repository.findAll();
	  }
}



