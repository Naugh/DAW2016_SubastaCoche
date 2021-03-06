package es.tiburon.code.follow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.tiburon.code.user.User;
import es.tiburon.code.user.UserRepository;


@RestController
@RequestMapping("/follow")
public class FollowController {
	private static final Logger log = LoggerFactory.getLogger(FollowController.class);
	
	@Autowired
	private FollowRepository fRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Collection<Follow> getFolow(){
		log.info("follow findAll");
		return fRepo.findAll();
	}
	
	
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Follow getFollow(@PathVariable String id){
		return fRepo.findByUser(uRepo.findOne(Long.parseLong(id)));
	}
	
	@RequestMapping(value = "/add/{idSeguidor}/{idSeguido}", method = RequestMethod.PUT)
	public ResponseEntity<Follow> addFollow(@PathVariable String idSeguidor, @PathVariable String idSeguido){
		User uSeguidor = uRepo.findOne(Long.parseLong(idSeguidor));
		User uSeguido = uRepo.findOne(Long.parseLong(idSeguido));
		if(uSeguido != null && uSeguidor != null){
			Follow seguidor = fRepo.findByUser(uSeguidor);
			Follow seguido = fRepo.findByUser(uSeguido);
			if(seguidor != null ){
				log.info("1 sigue a 3 (mod)");
				seguidor.getFollows().add(uSeguido);
				fRepo.save(seguidor);
			}else{
				log.info("1 sigue a 3 (nuevo)");
				seguidor = new Follow(uSeguidor);
				seguidor.getFollows().add(uSeguido);
				fRepo.save(seguidor);
			}
			
			if(seguido != null){
				log.info("3 seguido por 1 (mod)");
				seguido.getFollowers().add(uSeguidor);
				fRepo.save(seguido);
			}else{
				log.info("3 seguido por 1 (nuevo)");
				seguido = new Follow(uSeguido);
				seguido.getFollowers().add(uSeguidor);
				fRepo.save(seguido);
			}
			log.info("creada relacion {}-{}",idSeguidor,idSeguido);
			return new ResponseEntity<>(seguido,HttpStatus.OK);
		}else{
			log.info("usuario/s no validos");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(value = "/remove/{idSeguidor}/{idSeguido}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Follow> removeFollow(@PathVariable String idSeguidor, @PathVariable String idSeguido){
		
		User uSeguidor = uRepo.findOne(Long.parseLong(idSeguidor));
		User uSeguido = uRepo.findOne(Long.parseLong(idSeguido));
		if(uSeguido != null && uSeguidor != null){
			Follow seguidor = fRepo.findByUser(uSeguidor);
			Follow seguido = fRepo.findByUser(uSeguido);
			if(seguidor != null && seguido != null){
				seguidor.getFollows().remove(uSeguido);
				fRepo.save(seguidor);
				seguido.getFollowers().remove(uSeguidor);
				fRepo.save(seguido);
			}else{
				log.info("error eliminando relacion");
			}
			log.info("eliminada relacion {}-{}",idSeguidor,idSeguido);
			return new ResponseEntity<>(seguido,HttpStatus.OK);
		}else{
			log.info("usuario/s no validos");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/byUsers", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Collection<Follow> getFollowsByUsers(@RequestBody List<User> users){
		ArrayList<Follow> lista = new ArrayList<Follow>();
		for(User u : users){
			lista.add(fRepo.findByUser(uRepo.findOne(u.getId())));
		}
		return lista;
	}
	
}

















