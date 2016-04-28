import {Component}   from 'angular2/core';
import {ROUTER_DIRECTIVES,RouteParams, Router} from 'angular2/router';

@Component({
  selector: 'filtros-busqueda' ,
  directives: [ROUTER_DIRECTIVES],
  templateUrl: 'app/filtro.component.html'

})

export class FiltroComponent {

  private busqueda;
  private userProd;
  private desde;
  private hasta;
  private tipo;
  private ubicacion;
  private vendedorP:boolean;
  private vendedorE:boolean;

  private palabra;


  constructor(
    private router:Router,
    private rParams:RouteParams
  ){
    if(this.rParams.get('palabra')===null){
      this.userProd = 'product';
      this.tipo = 'ambos';
      this.vendedorP = true;
      this.vendedorE = true;
    }else{
      this.palabra = this.rParams.get('palabra');
      let busq = this.palabra.split('+');
      this.busqueda = busq[0];
      this.userProd = busq[1];
      this.desde = busq[2];
      this.hasta = busq[3];
      this.tipo = busq[4];
      this.ubicacion = busq[5];
      this.vendedorP = busq[6];
      this.vendedorE = busq[7];

    }

  }






  buscar(){
    let palabra = this.busqueda + '+' + this.userProd+ '+' +this.desde+ '+' +this.hasta+ '+' +this.tipo+ '+' +this.ubicacion+ '+' +this.vendedorP+ '+' +this.vendedorE;
    this.router.navigate(['Buscar',{palabra: palabra}]);
  }
}