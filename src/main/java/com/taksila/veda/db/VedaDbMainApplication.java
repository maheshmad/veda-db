package com.taksila.veda.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class VedaDbMainApplication 
{
	@Autowired
	ApplicationContext applicationContext;
	
	public static void main(String[] args) 
	{
		SpringApplication.run(VedaDbMainApplication.class, args);
				
	}
		
//	@Bean
//	public CommandLineRunner addRepo()
//	{
		
//		return (args)->	
//		{			
//			EventSessionsRepository repository = applicationContext.getBean(EventSessionsRepository.class,"demo");
//			EventSessionsRepository repository2 = applicationContext.getBean(EventSessionsRepository.class,"school1");
//			
//			EventSession a = new EventSession();
//			a.setUserRecordId("mm1");
//			EventSession b = new EventSession();
//			b.setUserRecordId("mm2");
//			
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);			
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);	
//			repository2.save(b);
//			repository.save(a);
//			repository2.save(b);
//						
//						
////			EventSession c = new EventSession();
////			c.setUserRecordId("mm3");
////			repository.save(c);			
////			repository.save(c);			
////			repository.save(c);			
////			repository.save(c);			
////			repository.save(c);
////						
////			EventSession d = new EventSession();
////			d.setUserRecordId("mm4");
////			repository2.save(d);			
////			repository2.save(d);			
////			repository2.save(d);			
////			repository2.save(d);			
////			repository2.save(d);
//			
////			for (EventSession eventSess: repository.findByUserRecordId("mm1"))
////			{
////				System.out.println("****** event uid = "+eventSess.getEventSessionId());
////			}
//			
//			
//			
//			
//		};
//	}
	
}
