Vue.component("administrator-pocetna", {
    data(){
        return{
            restaurant: {},
            articals: [],
            idRestaurant: null,
            selectedArtical: {}
        }
    },
template: `
    
<div class="containerInfo" >
   
        <div class="row-restaurants">
            <div class = "col-with-picture">
                <div class="col-picture">
                    <div><img v-bind:src="'pictures/'+restaurant.link" style="height:250px !important; width:300px !important"></div>
                </div>
            </div>
            <div class="col-info">
                <h4 class="text" style="color: black;">Naziv: {{restaurant.name}}</h4>
                <h4 class="text"style="color: black;" >Tip: {{restaurant.type}}</h4>
                <h4 class="text" style="color: black;">Lokacija: {{restaurant.address.street}} {{restaurant.address.number}}, {{restaurant.address.city}} {{restaurant.address.zipCode}}</h4>
                <h4 class="text" style="color: black;">Status: {{restaurant.status}}</h4>
                <h4 class="text" style="color: black;">Prosecna ocena: {{restaurant.grade}}</h4>
                <button type="button" class="btn btn-success" v-on:click="showComments"> Pogledaj komentare </button>
                <button type="button" class="btn btn-success" v-on:click="back"> Vrati se </button>
            </div>
        </div>
       
    <h4 style="margin-left: 15px;  font-weight: bold; "> ARTIKLI:  </h4> 

    
    <div id="artikli" class="tab-pane fade in active">
        <div class="containerInfo">
            <div class="tab-content">
                <div class="panel">
                    <div class="row-artical">
                        <div class="column" v-for="artical in articals" v-on:click="getSelectedArtical(artical)">
                            <div class="card">
                            <img v-bind:src="'pictures/'+  artical.link" style="height:280px !important; width:320px !important" >
                                <div class="container">
                                    <h2>{{artical.name}}</h2>
                                    <p class="title">{{artical.type}}</p>
                                    <p>Cena: {{artical.price}} din</p>
                                    <p>Gramaza: {{artical.quantity}}g</p>
                                    <p>Opis: {{artical.description}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  
</div>   

`,
methods:{
        getSelectedArtical: function(artical){
        this.selectedArtical = artical;
        },
        showComments: function() {
            this.$router.push({path: `/prikaziKomentareRestoranaAdminPocetna`, query:{ id: this.idRestaurant}})
        },
        back: function() {
            window.location.href = "index.html"
        }
},
mounted(){
        this.idRestaurant = this.$route.query.id,
        axios.post("/WebShopREST/rest/restaurant/getRestaurant", this.idRestaurant)
        .then( response => {
            this.restaurant = response.data
        })
        .catch(function(error){
            console.log(error)
        }),
        axios.get("/WebShopREST/rest/artical/getAllArticals")
        .then( response => {
            this.articals = response.data
        })
        .catch(function(error){
            console.log(error)
        })
}
});