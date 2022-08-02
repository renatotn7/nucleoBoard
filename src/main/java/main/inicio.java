package main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import functions.OcrProcessImage;
import functions.eventpojos.GcsEvent;
import vo.LoginVO;
import vo.Pagina;
import vo.ParagrafoVO;
import vo.PerguntaVO;
import vo.RespostaVO;
import vo.TokenVO;

//@SpringBootApplication
public class inicio {
	 private static String FUNCTIONS_BUCKET = "renatotn7hdocrimage";
	 public static RestTemplate restTemplate;
	 
	  private static final Logger logger = Logger.getLogger(
	      inicio.class.getName());

	 

	  private static OcrProcessImage sampleUnderTest;
	public static void main(String[] args) throws IOException {
	
		// TODO Auto-generated method stub
		  sampleUnderTest = new OcrProcessImage();
		
		 GcsEvent gcsEvent = new GcsEvent();
		    gcsEvent.setBucket(FUNCTIONS_BUCKET);
		    gcsEvent.setName("pagina.JPG");
		    
		    sampleUnderTest.accept(gcsEvent, null);
		    
		 
			/*	System.err.println("quantidade: "+quantidade+ "perguntas: "+perguntas);
				if(quantidade==4) {
					
						a_perguntas=perguntas.split("\n\n");
						for(String p:a_perguntas) {
							String pergunta = p.split("\n")[0];
							String resposta = p.split("\n")[1];
							System.out.println("pergunta:"+pergunta);
							System.out.println("reposta:"+resposta);
						}
					
					//String pergunta = a_perguntas.split("\n")[0];
				}
				if(quantidade==2) {
					
					a_perguntas=perguntas.split("\n\n");
					if(a_perguntas[0].length()>0) {
						HashMap<Integer,String> a =new HashMap<Integer,String> ();
						//String aa_perguntas=a_perguntas[0].split("\n");
						for(String p:a_perguntas[0].split("\n")) {
							a.put((int)p.charAt(0),p);
						}
						for(String r:a_perguntas[1].split("\n")) {
							String pergunta = a.get((int) r.charAt(0));
							String resposta = r;
							System.out.println("pergunta:"+pergunta);
							System.out.println("reposta:"+resposta);
						}
					}else {
						if(a_perguntas[1].split("\n")[0].split("\\?")[1].length()>1) {
							String conjunto[] = a_perguntas[1].split("\n");
							for(String item:conjunto) {
								String pergunta=item.split("\\?")[0];
								String resposta=item.split("\\?")[0];
								System.out.println("pergunta:"+pergunta);
								System.out.println("reposta:"+resposta);
							}
						}
					}
					
				}
				if(quantidade==1) {
					a_perguntas=perguntas.split("\n");
					for(int j=0;j<a_perguntas.length;i+=2) {
						String pergunta = a_perguntas[j];
						String resposta = a_perguntas[j+1];
						System.out.println("pergunta:"+pergunta);
						System.out.println("reposta:"+resposta);
					}
					
				}
				//if(perguntas.indexOf(")")!=-1) {
				//	a_perguntas=perguntas.split("\\.\r");
				//	
				//	
				//}
		    }*/
		   SpringApplication app = new SpringApplication(inicio.class);
	        app.setDefaultProperties(Collections
	          .singletonMap("server.port", "8083"));
	        //spring.main.web-application-type=none
	        app.run(args);
		
	
	}
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		LoginVO login = new LoginVO("admin","admin");
		//sampleUnderTest.outputOCR
		
		Pagina pag = new Pagina(null, 1, sampleUnderTest.outputOCR, null, new byte[1], "",
				null,1L) ;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println(pag);
			TokenVO quote = restTemplate.postForObject(
					"http://localhost:8082/api/authenticate",login, TokenVO.class);
			logger.info(quote.getId_token());
			
			headers.set("Authorization", "Bearer "+quote.getId_token());

			HttpEntity<Pagina> entity = new HttpEntity<>(pag,headers);
			Pagina response = restTemplate.postForObject(
					"http://localhost:8082/api/paginas",entity, Pagina.class);
			// Integer id, String texto,String pagina,Integer paginaId
			for(int i=0;i<sampleUnderTest.paragrafos.size();i++) {
			//for(Integer key:sampleUnderTest.paragrafos) {
				String grpPerguntas = sampleUnderTest.grp_perguntas.get(i);
				String paragrafo=sampleUnderTest.paragrafos.get(i);
			
		//	  for(int i=0;i<sampleUnderTest.listPerguntas.size();i++) {
			    	
				  ParagrafoVO pv = new  ParagrafoVO(null, i, paragrafo, null, null,
						  response.getId()) ;
				  System.out.println(pv);
					  HttpEntity<ParagrafoVO> entityPar = new HttpEntity<>(pv,headers);
					  ParagrafoVO paragrafoVO = restTemplate.postForObject(
								"http://localhost:8082/api/paragrafos",entityPar, ParagrafoVO.class);
				
					
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
						System.out.println("pergunta:"+pergunta1);
						System.out.println("reposta:"+resposta1);


						
						/*RespostaVO respostav=new RespostaVO(null,resposta1);
						System.out.println("pagina "+response.getId());
						HttpEntity<RespostaVO> entrespostav = new HttpEntity<>(respostav,headers);
						RespostaVO repostaresp = restTemplate.postForObject(
								"http://localhost:8080/api/respostas",entrespostav, RespostaVO.class);
					*/
						
						
						
						
						PerguntaVO pergunta=new PerguntaVO(null,pergunta1,resposta1,paragrafoVO.getId());
						System.out.println(pergunta);
						//pergunta.setRespostaId(repostaresp.getId());
						System.out.println("pagina "+response.getId());
						
						HttpEntity<PerguntaVO> entityP = new HttpEntity<>(pergunta,headers);
						PerguntaVO perguntaresp = restTemplate.postForObject(
								"http://localhost:8082/api/perguntas",entityP, PerguntaVO.class);
						System.out.println("pergunta "+perguntaresp.getId());
						
						
						
						
					}
					
				
			    }
		/*	for(int i=0;i<sampleUnderTest.listPerguntas.size();i++) {
				String perguntas=sampleUnderTest.listPerguntas.get(i);
				
				String[] a_perguntas;
				if(perguntas.indexOf(")")!=-1) {
					a_perguntas=perguntas.split("\\.\r");
					
					for(String pergunta1:a_perguntas) {
						pergunta1=pergunta1.replaceAll("\n\n","_");
						
						if(pergunta1.charAt(0)=='_') {
							pergunta1=pergunta1.substring(1);
						}
						String[] partes=pergunta1.split("\\_");
						for(String p:partes) {
							int pos=	p.indexOf("\n");
							
							String pergdefato=p.substring(0,pos);
							String resposta;
							
							resposta=p.substring(pos+1);//,//pos+pergunta1.substring(pos).indexOf('.'));
							
							 
							
							RespostaVO respostav=new RespostaVO(null,resposta);
							System.out.println("pagina "+response.getId());
							HttpEntity<RespostaVO> entrespostav = new HttpEntity<>(respostav,headers);
							RespostaVO repostaresp = restTemplate.postForObject(
									"http://localhost:8080/api/respostas",entrespostav, RespostaVO.class);
							
							
							PerguntaVO pergunta=new PerguntaVO(null,pergdefato,null,response.getId());
							pergunta.setRespostaId(repostaresp.getId());
							System.out.println("pagina "+response.getId());
							
							HttpEntity<PerguntaVO> entityP = new HttpEntity<>(pergunta,headers);
							PerguntaVO perguntaresp = restTemplate.postForObject(
									"http://localhost:8080/api/perguntas",entityP, PerguntaVO.class);
							System.out.println("pergunta "+perguntaresp.getId());
							
							
							
							System.out.println("resposta "+repostaresp.getId());
						}
					}
				}
				if(perguntas.indexOf("1)")!=-1) {
					
				}
			
			}*/
			logger.info(quote.getId_token());
		return null;
		
	}

}
