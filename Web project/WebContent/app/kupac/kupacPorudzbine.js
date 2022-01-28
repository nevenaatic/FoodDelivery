Vue.component("porudbine-kupac", {
    data: function() {  
        return {
        kupac:{},
        orders: [],
        idKupca: null, 
        selected:{},
        mode: true,
        check: false
        }
    },
template: `
<div class="container" style=" margin-top: 20px; margin-left: 40px; margin-right: 10px;">
  
<div>
<table  style=" margin:25px 25px; font-size:1.1 em;"> 
<tr>
    <td > <input type="text" placeholder="naziv restorana" style="height:32px;"> </td>
    <td style="padding: 12px;"> Cena od: </td> 
    <td style="padding: 12px;"> <input type="text" placeholder="pocetni iznos" style="height:32px;"></td> 
    <td style="padding: 12px;"> do: </td> 
    <td style="padding: 12px;"> <input type="text" placeholder="krajnji iznos" style="height:32px;"></td> 
    <td style="padding: 12px;"> Datum od: </td>
    <td style="padding: 12px;"> <input type="date" style="height:32px;"></td> 
    <td style="padding: 12px;"> do: </td> 
    <td style="padding: 12px;"> <input type="date" style="height:32px;"></td> 
    <td> <button class="btn btn-danger" type="button" >Nadji</button> </td>

</tr>

</table>

<table style=" margin:25px 25px; font-size:1.1 em;"> 
<tr> 
        <td style="width:450px !important;"><p> Ukoliko zelite da filtrirate prikaz, odaberite odgovarajuci tip restorana </p></td>  
    
        <td > <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" > Tip restorana </button>                  
            <span class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <button class="dropdown-item" type="button" v-on:click="filterType('ITALIJANSKI')" >Italijanski</button>
            <button class="dropdown-item" type="button" v-on:click="filterTypeRestaurant('KINESKI')">Kineski</button>
            <button class="dropdown-item" type="button" v-on:click="filterTypeRestaurant('PIZZA')" >Pica</button>
            <button class="dropdown-item" type="button" v-on:click="filterTypeRestaurant('ROSTILJ')">Rostilj</button>
            <button class="dropdown-item" type="button" v-on:click="filterTypeRestaurant('RIBLJI')" >Riblji</button>
            <button class="dropdown-item" type="button" v-on:click="filterTypeRestaurant('VEGE')">Veganski</button>
            </span>
        </td>
        <td style="width:20px;"> </td>
      <td > <p> ili status porudzbine </p></td>  
      <td style="width:20px;"> </td>
      <td > <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" > Tip porudzbine </button>                  
            <span class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <button class="dropdown-item" type="button" v-on:click="filterStatus('OTKAZANA')">Otkazana</button>
            <button class="dropdown-item" type="button" v-on:click="filterStatus('OBRADA')">Obradjuje se</button>
            <button class="dropdown-item" type="button" v-on:click="filterStatus('CEKA_DOSTAVLJACA')">Ceka dostavljaca</button>
            <button class="dropdown-item" type="button" v-on:click="filterStatus('U_TRANSPORTU')">U transportu</button>
            <button class="dropdown-item" type="button" v-on:click="filterStatus('DOSTAVLJENA')">Dostavljena</button>
            <button class="dropdown-item" type="button" v-on:click="filterStatus('U_PRIPREMI')">U pripremi</button>
            </span>
        </td>
        <td style="width:50px;"> </td>
        <td><button class="btn btn-secondary" type="button"  v-if="check" v-on:click="reset()">x</button> </td> 
    </tr>
</table>

<table style=" margin:25px 25px; font-size:1.1 em;"> 
<tr> 
        <td style="width:450px !important;"><p> Ukoliko zelite da sortirate prikaz, odaberite odgovarajuci kriterijum  </p></td>  
    
        <td > <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" > Sortiraj porudzbine  </button>                  
            <span class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <button class="dropdown-item" type="button" v-on:click="sortNameAsc()">Nazivu restorana rastuce</button>
            <button class="dropdown-item" type="button" v-on:click="sortNameDesc()">Nazivu restorana opadajuce</button>
            <button class="dropdown-item" type="button" v-on:click="sortPriceAsc()">Ceni porudzbine rastuce</button>
            <button class="dropdown-item" type="button" v-on:click="sortPriceDesc()">Ceni porudzbine opadajuce</button>
            <button class="dropdown-item" type="button"  v-on:click="sortDateAsc()">Datumu porudzbine rastuce</button>
            <button class="dropdown-item" type="button" v-on:click="sortDateDesc()">Datumu porudzbine opadajuce</button>
            </span>
        </td>
        
    </tr>
</table>
</div>



   
  <div v-if="orders != []" > 
  <h3 style=" margin-left: 30px;"> <small> Trenutno stanje svih Vasih porudzbina: </small> <hr></h3>
    <table class="table table-hover" >
        <thead v-if= "orders != []">
          <tr>
            <th scope="col">Ime restorana</th>
            <th scope="col">Datum kreiranja</th>
            <th scope="col">Ukupna cena</th>
            <th scope="col">Status porudzbine</th>
            <th scope="col">Tip restorana </th>
            <th scope="col"> </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" v-on:click="getSelected(order)">
            <td>{{order.restaurantName}}</td>
            <td>{{order.date | dateFormat('DD.MM.YYYY.')}}</td>
            <td>{{order.price}}</td>
            <td>{{order.status}}</td>
            <td>{{order.restaurantType}}</td>
        
            <div>

              <td>
               <button type="button" class="btn btn-secondary" v-if="order.status == 'OTKAZANA'" v-on:click="getSelected(order)" data-toggle="modal" data-target="#brisanje" >Izbrisi</button>  </td>
               <button type="button" class="btn btn-secondary" v-if="order.status == 'DOSTAVLJENA'" v-on:click="getSelected(order)" data-toggle="modal" data-target="#brisanje" >Izbrisi</button> 
               </div>
          </tr>
        </tbody>
    </table>
  </div>

  <!-- modal obrisi-->
  <div class="modal fade" id="brisanje" role="dialog" >
          <div class="modal-dialog" style="width: 300px;" >
              <!-- Modal content -->
              <div class="modal-content">
                  <div class="modal-header" style="padding:35px 50px;">
                  <h5 class="modal-title" id="exampleModalLabel">Obrisi porudzbinu</h5>
                  </div>
                  <div class="modal-body"  style="padding:40px 50px;">
                      <form role="form" @submit="deleteOrder">
                        <div> <p> Da li ste sigurni da zelite da obrisete ovu porudzbinu?</p></div>
                          <button type="submit" class="btn btn-danger btn-block" v-on:click="deleteOrder"><span class="glyphicon glyphicon-off"></span> Obrisi</button>
                      </form>
                  </div>
                  <div class="modal-footer">
                  <button type="button" class="btn btn-danger btn-default pull-left"  data-dismiss="modal">Odustani</button>   
                  </div>
              </div>
          </div>
  </div>
  
</div>
`,
methods:{
    getSelected: function(order){
        this.selected = order;
      }, 
    deleteOrder() {
        axios.post("/WebShopREST/rest/order/deleteOrder", this.selected.id )
        .then(response => {
            router.push(`/porudzbine`);
        })
        .catch(function(error){
            console.log(error)
        })
    }, 
    changeStatus() {
       
        axios.post("/WebShopREST/rest/order/changeStatusCancel", this.selected.id)
        .then(response => {
            router.push(`/porudzbine`);
        })
        .catch(function(error){
            console.log(error)
        })
    },
     
    filterStatus: function (type){
        this.orders = this.orders.filter(order => order.status === type);
        
        this.check = true
    },
    
    filterType: function (type){
        this.orders = this.orders.filter(order => order.restaurantType === type);
        
        this.check = true
    },
    sortNameAsc: function() {
        function compare(a, b) {
          if (a.restaurantName < b.restaurantName)
            return -1;
          if (a.restaurantName > b.restaurantName)
            return 1;
          return 0;
        }

        return this.restaurants.sort(compare);
    }, 
    sortNameDesc: function() {
        function compare(a, b) {
          if (a.restaurantName < b.restaurantName)
            return 1;
          if (a.restaurantName > b.restaurantName)
            return -1;
          return 0;
        }
        
        return this.restaurants.sort(compare);
    },
    
    sortPriceDesc: function() {
        function compare(a, b) {
          if (a.price < b.price)
            return 1;
          if (a.price > b.price)
            return -1;
          return 0;
        }
        
        return this.restaurants.sort(compare);
    },
    sortPriceAsc: function() {
        function compare(a, b) {
          if (a.price < b.price)
            return -1;
          if (a.price > b.price)
            return 1;
          return 0;
        }

        return this.restaurants.sort(compare);
    },
    
    sortDateDesc: function() {
        function compare(a, b) {
          if (a.date < b.date)
            return 1;
          if (a.date > b.date)
            return -1;
          return 0;
        }
        
        return this.restaurants.sort(compare);
    },
    sortDateAsc: function() {
        function compare(a, b) {
          if (a.price < b.price)
            return -1;
          if (a.price > b.price)
            return 1;
          return 0;
        }

        return this.restaurants.sort(compare);
    },
    reset:function() {
        
    axios.get("/WebShopREST/rest/profile/profileUser")
    .then( response => {
        this.kupac = response.data
    })
    .catch(function(error){
        console.log(error)
    }),
    axios.get("/WebShopREST/rest/order/getOrders")
    .then( response => {
        this.orders = response.data,
        this.check = false;
    })
    .catch(function(error){
        console.log(error)
    })
    }
},
mounted(){
    axios.get("/WebShopREST/rest/profile/profileUser")
    .then( response => {
        this.kupac = response.data
    })
    .catch(function(error){
        console.log(error)
    }),
    axios.get("/WebShopREST/rest/order/getOrders")
    .then( response => {
        this.orders = response.data,
        this.check = false;
    })
    .catch(function(error){
        console.log(error)
    })

},

filters: {
    dateFormat: function(value, format){
        var parsed = moment(value);
        return parsed.format(format)
    }
}

});