package com.taksila.veda.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.taksila.veda.db.eventsessions.EventSessionsRepository;
import com.taksila.veda.model.db.event_session.v1_0.EventSession;

@SpringBootApplication
public class VedaDbMainApplication 
{
	@Autowired
	ApplicationContext applicationContext;
	
	public static void main(String[] args) 
	{
		SpringApplication.run(VedaDbMainApplication.class, args);
				
	}
		
	@Bean
	public CommandLineRunner addRepo()
	{
		
		return (args)->	
		{			
			EventSessionsRepository repository = applicationContext.getBean(EventSessionsRepository.class,"demo");
			EventSessionsRepository repository2 = applicationContext.getBean(EventSessionsRepository.class,"school1");
			
			EventSession a = new EventSession();
			a.setEventSessionId("45454545454324344");
			a.setUserRecordId("mm1");
			EventSession b = new EventSession();
			b.setEventSessionId("4545454545432434adsfadsa4");
			b.setUserRecordId("mm2");
			
			repository.save(a);			
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);			
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);	
			repository2.save(b);
			repository.save(a);
			repository2.save(b);
						
						
			EventSession c = new EventSession();
			c.setUserRecordId("mm3");
			c.setEventSessionId("4545454dsadfadfadf87asdfad44");
			repository.save(c);			
			repository.save(c);			
			repository.save(c);			
			repository.save(c);			
			repository.save(c);
						
			EventSession d = new EventSession();
			d.setUserRecordId("mm4");
			d.setEventSessionId("4545454dsadfadfadf87aadsasdfad44");
			repository2.save(d);			
			repository2.save(d);			
			repository2.save(d);			
			repository2.save(d);			
			repository2.save(d);			
			for (EventSession eventSess: repository.findByUserRecordId("mm1"))
			{
				System.out.println("****** event uid = "+eventSess.getEventSessionId());
			}
			
			
			
			
		};
	}
	
}
