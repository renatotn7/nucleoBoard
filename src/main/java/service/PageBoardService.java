package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import enumerator.TipoAnexo;
import main.StringWorks;
import vo.AnexoVO;
import vo.Pagina;
import vo.ParagrafoVO;
import vo.PerguntaVO;
import vo.ResponseDavid;
import vo.RespostaServerVO;
import vo.TokenVO;

public class PageBoardService {

	public static void main(String... args) {
		try {
			// ok  ParagrafoVO p =( new PageBoardService()).perguntasDiscursivas("Gates nasceu em uma família de classe média de Seattle. Seu pai, William H. Gates, era advogado de grandes empresas, e sua mãe, Mary Maxwell Gates, foi professora da Universidade de Washington e diretora de bancos. Bill Gates e as suas duas irmãs, Kristanne e Libby, frequentaram as melhores escolas particulares de sua cidade natal, e Bill também participou do Movimento Escoteiro ainda quando jovem. Bill Gates,[10] foi admitido na prestigiosa Universidade Harvard, (conseguindo 1 590 SATs dos 1 600 possíveis[11]) mas abandonou os cursos de Matemática e Direito no terceiro ano,[12] para dedicar-se à Microsoft.");
			 // ok ParagrafoVO p =( new PageBoardService()).perguntasMultiplaEscolha("Gates nasceu em uma família de classe média de Seattle. Seu pai, William H. Gates, era advogado de grandes empresas, e sua mãe, Mary Maxwell Gates, foi professora da Universidade de Washington e diretora de bancos. Bill Gates e as suas duas irmãs, Kristanne e Libby, frequentaram as melhores escolas particulares de sua cidade natal, e Bill também participou do Movimento Escoteiro ainda quando jovem. Bill Gates,[10] foi admitido na prestigiosa Universidade Harvard, (conseguindo 1 590 SATs dos 1 600 possíveis[11]) mas abandonou os cursos de Matemática e Direito no terceiro ano,[12] para dedicar-se à Microsoft.");
			ParagrafoVO p =( new PageBoardService()).textoEmTopicos("Gates nasceu em uma família de classe média de Seattle. Seu pai, William H. Gates, era advogado de grandes empresas, e sua mãe, Mary Maxwell Gates, foi professora da Universidade de Washington e diretora de bancos. Bill Gates e as suas duas irmãs, Kristanne e Libby, frequentaram as melhores escolas particulares de sua cidade natal, e Bill também participou do Movimento Escoteiro ainda quando jovem. Bill Gates,[10] foi admitido na prestigiosa Universidade Harvard, (conseguindo 1 590 SATs dos 1 600 possíveis[11]) mas abandonou os cursos de Matemática e Direito no terceiro ano,[12] para dedicar-se à Microsoft.");
			int i = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ParagrafoVO perguntasDiscursivas(String ptexto) {
		String comando="Crie 4 perguntas discursivas com reposta no próprio texto abaixo e suas respectivas respostas corretas. as perguntas devem estar precedidas de P_ mais o número e as respostas de R_ mais o número escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		//P_ pergunta estabilizado
		//R_Resposta
		return separaRepostasEPerguntas(resposta.getItemparagrafo(), resposta.getParagrafo());
		//return separaRepostasEPerguntas(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	public ParagrafoVO perguntasMultiplaEscolha(String ptexto) {
		String comando="Crie 4 perguntas multipla escolhas com 4 possíveis respostas(uma por linha) baseadas neste texto, essas 4 opções devem vir seguidas da letra que representa a opção correta de sua verdadeira resposta. As perguntas devem estar precedidas de P_ e o número. No final de cada conjunto das 4 opções a resposta deve vir precedida de R_ e apenas sua letra.   escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		//Penunciado estabilizado
		//R_reposta
		return separaRepostasEPerguntas(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	public ParagrafoVO textoSimplificado(String ptexto) {
		String comando="Explique e reorganize o seguinte texto simplificando para a leitura de uma criança palpérrima e quase analfabeta e sem informação entender; E também substitua palavras por seus sinônimos mais simples, pois se trata de facilitar o texto; além do mais essa explicação esclarescedora que você fornecerá deve se ater a verdade e deve evitar afirmações duvidosas ou que dê margem para erro; E também remova informações irrelevantes para o ponto central do texto e evite desvios do assunto principal; E também o texto deve ser menor que o original; E também os objetos do textos devem ser todos mencionados; E também caso se trate de uma enumeração deve vir linha por linha; E também transcreva as siglas que estão escritas em letras maiúsculas caso não esteja a transcrição no texto; E também reorganize o texto de preferencia através de reagrupamentos para evitar  repetições de palavras. escreva com um português simples:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		/**
		 * Temperatura
	0.88
	Comprimento máximo 
	389
	Parar sequências
	Digite a sequência e pressione Tab
	Parte superior
	1
	Penalidade de frequência
	0
	Penalidade de presença
	2

		 */
		return addAnexo(resposta.getItemparagrafo(), resposta.getParagrafo(),TipoAnexo.TXTSIMPLIFICADO.getId());
	}
	
	
	public ParagrafoVO textoEmTopicos(String ptexto) {
		String comando="Reescreva o seguinte texto em tópicos para uma criança palpérrima quase iletrada entender. escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		/*Temperatura
0.88
Comprimento máximo
389
Parar sequências
Digite a sequência e pressione Tab
Parte superior
1
Penalidade de frequência
0
Penalidade de presença
2
Melhor de
1
*/
		return addAnexo(resposta.getItemparagrafo(), resposta.getParagrafo(),TipoAnexo.TXTTOPICOS.getId());
	}
	public ParagrafoVO expliqueOSeguinteTexto(String ptexto) {
		String comando="Explique o seguinte texto para uma criança palpérrima quase iletrada entender. escreva em português";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return addAnexo(resposta.getItemparagrafo(), resposta.getParagrafo(),TipoAnexo.EXPLICATEXTO.getId());
	}
	
	public ParagrafoVO expliqueOSeguinteTextoComTitulos(String ptexto) {
		String comando="Gere de 4 a 8 entidade seguidos de ':'(é importantíssimo que explique baseado no texto os fatos ligados a cada entidade depois de cada sinal de ':')   as entidades e explicações devem se encontrar somente no seguinte texto sem trazer outras informações externas. escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return addAnexo(resposta.getItemparagrafo(), resposta.getParagrafo(),TipoAnexo.EXPLICATEXTOCTIT.getId());
	}
	
	public ParagrafoVO deixarAtraenteJornalistico(String ptexto) {
		String comando="Deixe esse texto mais atraente de forma jornalistica. explicando o significado de todas as siglas e termos técnicos. escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	
	public ParagrafoVO qualopublico(String ptexto) {
		String comando="Para quais audiências este texto é? escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	public ParagrafoVO sequenciaDeAcontecimentos(String ptexto) {
		String comando="relacione a sequencia de acontecimentos aos sujeitos do texto abaixo. responda em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	public ParagrafoVO tituloConvidativo(String ptexto) {
		String comando="Crie um título convidativo para este texto. escreva em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	//nao funciona  tao bem
	public ParagrafoVO criatabeladefatos(String ptexto) {
		String comando="Uma tabela com 5 linhas sumarizando: O quê(entidade associada),atividade(ação associada), Como(a forma que foi realizada a atividade), porquê(motivo pelo qual foi realizada a atividade), quando(em que momento foi realizada a atividade), objetivo(objetivo da atividade) e onde(local do acontecimento), efeito(efeito da atividade).As linhas devem ser baseadas exclusivamente no texto abaixo. Responda em português:";
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	

		public ParagrafoVO criaSlogansBaseadosNoTexto(String ptexto) {
			String comando="crie slogans para uma empresa de harmonização facial persuasivos baseados no seguinte texto em português:";
					
			RespostaServerVO resposta=	consultaService( ptexto, comando);
			
			return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
		}
	public ParagrafoVO extraiTagsDoTexto(String ptexto) {
		String comando="Extract keywords from this text, answer in portuguese:";
				
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
		
	public ParagrafoVO criaMusica(String ptexto) {
		String comando="crie uma música evangélica com rimas, que possa fazer muito sucesso. Em português:";
				
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
		
	public ParagrafoVO layoutsECoresParaPublicidade(String ptexto,String tema,String nomeProfissao,String oqueFaz,String comofaz ) {
		String comando="Indique cores ideais para um layout baseado no texto abaixo.  Em português:"
				+ "Considerando o tema ,este "+nomeProfissao +" faz "+oqueFaz + " da seguinte forma: "+ comofaz;
		//depois tem que colocar as palavras cores: paletas de cores: layout:
		
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	
	public ParagrafoVO cricaodeCopyWebSite(String ptexto) {
		String comando="Write a creative ad copy webpage for the following product:  Em português:"
				+ "Produto:  "+ptexto ;
		//depois tem que colocar as palavras cores: paletas de cores: layout:
		
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
		
	public ParagrafoVO cricaodeVideoScript(String ptexto) {
		String comando="Write a creative ad presentation video script for the following product. Escreva em português:"
				+ "Produto:  "+ptexto ;
		//depois tem que colocar as palavras cores: paletas de cores: layout:
		
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	public ParagrafoVO cricaodeVideoScriptPlanning(String ptexto) {
		String comando="Write a project for a creative video scenes transitions for the following product. Escreva em português:"
				+ "Produto:  "+ptexto ;
		//depois tem que colocar as palavras cores: paletas de cores: layout:
		
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	
	public ParagrafoVO cricaodeVideoScriptPlanningv2(String ptexto) {
		String comando="Write a project for a \"creative video scenes\" with \"transitions planning\" for the following product. Escreva em português:"
				+ "Produto:  "+ptexto ;
		//depois tem que colocar as palavras cores: paletas de cores: layout:
		
		RespostaServerVO resposta=	consultaService( ptexto, comando);
		
		return separaRepostasEPerguntasOld(resposta.getItemparagrafo(), resposta.getParagrafo());
	}
	
	/***
	 * O código acima é um exemplo de uma possível implementação de um servidor que utilize a API do OpenAI para gerar perguntas a partir de um texto fornecido. Este servidor pode ser consultado passando-se o texto e o comando desejados como parâmetros e retornará uma RespostaServerVO contendo a lista de perguntas geradas, bem como os parágrafos do texto original.
	 * @param ptexto
	 * @param comando
	 * @return
	 */
	public RespostaServerVO consultaService(String ptexto,String comando){
		List<String> grp_perguntas = new ArrayList<String>();
		List<String> paragrafos = new ArrayList<String>();


		  List<String> listPerguntas=new ArrayList<String>();
		RespostaServerVO rvo = new RespostaServerVO();
		
		 
		   StringBuilder sb= new StringBuilder();
		   String linha = ptexto;
			  
			   System.out.println(linha);
			   if(linha.split(" ").length>10) {
				   String tjson = "{\r\n" + 
					   		"  \"prompt\": \""+comando+"\\n\\n"+ linha+"\\n\\n\",\r\n" + 
					   		"  \"temperature\": 0.5,\r\n" + 
					   		"  \"max_tokens\": 2000,\r\n" + 
					   		"  \"top_p\": 1,\r\n" + 
					   		"  \"frequency_penalty\": 1,\r\n" + 
					   		"  \"presence_penalty\": 1\r\n" + 
					   		"}";
				   rvo.setParagrafo(linha);
				   System.out.println("(((((((((((((((((->"+tjson);
				  // paragrafos.add(linha);
			    HttpClient hclient = HttpClient.newBuilder().version(Version.HTTP_2).build();
				 HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.openai.com/v1/engines/text-davinci-002/completions"))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer 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")
			   .POST(HttpRequest.BodyPublishers.ofString(tjson)).build();
			
					try {
						HttpResponse<String> response = hclient.send(request,HttpResponse.BodyHandlers.ofString());
						
						
						Gson gson = new GsonBuilder().create();
						
						ResponseDavid rdavid = gson.fromJson(response.body(), ResponseDavid.class);
						System.out.println("****"+rdavid.getChoices().get(0).getText()+"*****");
						rvo.setItemparagrafo(rdavid.getChoices().get(0).getText())	;//					grp_perguntas.add(rdavid.getChoices().get(0).getText());
						//listPerguntas.add(rdavid.getChoices().get(0).getText());
						
						
						
						
						
						sb.append(response.body()+"\n");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
			   }
	
		  // rvo.setGrp_perguntas(grp_perguntas);
		//	rvo.setListPerguntas(listPerguntas);
		//	rvo.setParagrafos(paragrafos);
		
			 return rvo;
}
	
	public ParagrafoVO separaRepostasEPerguntasOld(String itemparagrafo,String pparagrafo) {

		
		//	for(int i=0;i<paragrafos.size();i++) {
			
				String grpPerguntas = itemparagrafo;
				String paragrafo=pparagrafo;
				
		//
				List<PerguntaVO> sperguntas = new ArrayList<>();
				  ParagrafoVO pv = new  ParagrafoVO(null, 0, paragrafo, null, sperguntas,
						  null) ;
				//  sparagrafos.add(pv);
				 
					 
					  
				
					
					String perguntas=grpPerguntas;
					
					ArrayList<String>arr_perguntas =new ArrayList<String>(); 
					ArrayList<String>arr_respostas =new ArrayList<String>(); 
					
					String[] itens=perguntas.split("\n");
					for(String item: itens) {
						if(item.contains("P_")) {
							arr_perguntas.add(item);
						}else
							if(item.contains("R_")) {
								arr_respostas.add(item);
							}
					}
					for(int z = 0; z<arr_perguntas.size();z++) {
						String pergunta1	=arr_perguntas.get(z).trim();
						String resposta1 =arr_respostas.get(z).trim();
						PerguntaVO pergVO=new PerguntaVO();
						pergVO.setQuestao(pergunta1);
						pergVO.setResposta(resposta1);
						sperguntas.add(pergVO);
					//	System.out.println("pergunta:"+pergunta1);
					//	System.out.println("reposta:"+resposta1);

					}
					
				
		//	    }
			return pv;
	}
	
	
public ParagrafoVO addAnexo(String itemparagrafo,String pparagrafo, int tipoAnexo) {
				String paragrafo=pparagrafo;
				List<PerguntaVO> sperguntas = new ArrayList<>();
				  ParagrafoVO pv = new  ParagrafoVO(null, 0, paragrafo, null, sperguntas,
						  null) ;
					  AnexoVO anexo = new AnexoVO();
					  anexo.setTexto(itemparagrafo);
					  anexo.setTipo(tipoAnexo);
					  List<AnexoVO> anexos=new ArrayList<AnexoVO>();
					  anexos.add(anexo);
					  pv.setAnexos(anexos);
					
			return pv;
	}
	
	
	public ParagrafoVO separaRepostasEPerguntas(String itemparagrafo,String pparagrafo) {

		
		//	for(int i=0;i<paragrafos.size();i++) {
			
				String grpPerguntas = itemparagrafo;
				String paragrafo=pparagrafo;
				
		//
				List<PerguntaVO> sperguntas = new ArrayList<>();
				  ParagrafoVO pv = new  ParagrafoVO(null, 0, paragrafo, null, sperguntas,
						  null) ;
				//  sparagrafos.add(pv);
				 
					  
				
					
					String perguntas=grpPerguntas;
					
					ArrayList<String>arr_perguntas =new ArrayList<String>(); 
					ArrayList<String>arr_respostas =new ArrayList<String>(); 
					
					String[] itens=perguntas.split("\n");
					boolean onP=false;
					boolean onR=false;
					StringBuilder sbp = new StringBuilder(); 
					StringBuilder sbr = new StringBuilder();
					for(String item: itens) {
						
						if(item.contains("P_")) {
							onP=true;
							onR=false;
							
						//	arr_perguntas.add(item);
						}else
							if(item.contains("R_")) {
								onR=true;
								onP=false;
								//arr_respostas.add(item);
								sbr.append(item);
							}
						if(onP) {
							sbp.append(item+"\n");
						}
						if(onR) {
							arr_perguntas.add(sbp.toString());
							arr_respostas.add(sbr.toString());
							sbp = new StringBuilder();
							sbr = new StringBuilder();
							onP=false;
							onR=false;
						}
					}
					for(int z = 0; z<arr_perguntas.size();z++) {
						String pergunta1	=arr_perguntas.get(z).trim();
						String resposta1 =arr_respostas.get(z).trim();
						PerguntaVO pergVO=new PerguntaVO();
						pergVO.setQuestao(pergunta1);
						pergVO.setResposta(resposta1);
						sperguntas.add(pergVO);
					//	System.out.println("pergunta:"+pergunta1);
					//	System.out.println("reposta:"+resposta1);

					}
					
				
		//	    }
			return pv;
	}
}
