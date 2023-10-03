package med.voll.api.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico, UriComponentsBuilder uriComponentsBuilder){
       // System.out.println("El request llega correctamente ");
       // System.out.println(datosRegistroMedico);
        Medico medico= medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico=new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        //URI url="http://localhost:8080/medicos/"+medico.getId();
        URI url=uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }
    /*
      @PostMapping
    public void registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico){
       // System.out.println("El request llega correctamente ");
       // System.out.println(datosRegistroMedico);
        medicoRepository.save(new Medico(datosRegistroMedico));
    }
     */
    @GetMapping
    //public List<Medico> ListarMedicos(){
    //public List<DatosListadoMedico> ListarMedicos(){
    //public Page<DatosListadoMedico> ListarMedicos(Pageable paginacion){
    public ResponseEntity<Page<DatosListadoMedico>> ListarMedicos(@PageableDefault(size=2) Pageable paginacion){
        //return medicoRepository.findAll();
        //return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }
    /*
      @GetMapping
    //public List<Medico> ListarMedicos(){
    //public List<DatosListadoMedico> ListarMedicos(){
    //public Page<DatosListadoMedico> ListarMedicos(Pageable paginacion){
    public Page<DatosListadoMedico> ListarMedicos(@PageableDefault(size=10) Pageable paginacion){
        //return medicoRepository.findAll();
        //return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new);
    }
     */
    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
    Medico medico=medicoRepository.getReferenceById(datosActualizarMedico.id());
    medico.actualizarDatos(datosActualizarMedico);
    return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento())));
    }
    /*
    //2
     @PutMapping
    @Transactional
    public void actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
    Medico medico=medicoRepository.getReferenceById(datosActualizarMedico.id());
    medico.actualizarDatos(datosActualizarMedico);
    }
     */
    //delete base de datos 1
   /*
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }
    */

    //delete logico

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity desactivarMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }
    /*
    @DeleteMapping("/{id}")
    @Transactional
    public void desactivarMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
    }
    */
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id){
        Medico medico=medicoRepository.getReferenceById(id);
        var datosMedico=new DatosRespuestaMedico(medico.getId(), medico.getNombre(), medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }
}
