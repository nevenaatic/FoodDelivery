Vue.component("administrator-changeRestaurant", {
    data:function(){
        return{
            restaurant: {},
            managers: [],
            manager: {},
            id: null,
            type: "",
            city:"",
            number:"",
            street:"",
            zipCode:"",
            link: ""
        }
    },    
  template: `
  <div class="containerInfo t"> 
    <div class="information">
      <form>
          <table class="t">
              <tr>
              <td class="labela">Novi menadzer:</td>
              <td><select class="form-control" v-model="restaurant.managerId" placeholder="Kliknite za izbor menadzera">
                  <option v-for="m in managers" v-bind:value="m.id">{{m.name}} {{m.surname}}</option>
                  </select>
              </td>
              <td class="buttonMap"><button type="button" class="btn btn-success" v-on:click="dodajMenadzera" style="margin-left:20px">Kreiraj novog menadzera</button></td>
              <td><p style="margin-left: 15px;margin-top: 8px;">*Ukoliko ne postoje slobodni menadzeri klikom na ovo dugme kreirajte novog menadzera.</p></td>
              </tr>
              <tr>
              <td class="labela">Naziv:</td>
              <td><input class="form-control" type="text" placeholder="Naziv" v-model="restaurant.name" v-bind:value="name">{{name}}</td>
              </tr>
              <tr>
              <td class="labela">Tip:</td>
              <td><select  v-model="restaurant.type" placeholder="Kliknite za izbor tipa" style="height: 35px; width: 200px; background-color:#5CB85C; color:white; border-radius: 4px; font-size: 14px;">
                <option value="ITALIJANSKI">Italijanski</option>
                <option value="KINESKI">Kineski</option>
                <option value="PIZZA">Pica</option>
                <option value="ROSTILJ">Rostilj</option>
                <option value="RIBLJI">Riblji</option>
                <option value="VEGE">Veganski</option>
              </select>
              </td>
              </tr>
              <tr>
              <td class="labela">Ulica:</td>
              <td><input class="form-control" type="text" placeholder="Ulica" v-model="restaurant.address.street"  v-bind:value="street">{{street}}</td>
              <td class="buttonMap"><button type="button" class="btn btn-success" style="margin-left:20px"><i></i>Choose on map</button></td>
              </tr>
              <tr>
              <td class="labela">Broj:</td>
              <td><input class="form-control" type="number" placeholder="Broj" v-model="restaurant.address.number"  v-bind:value="number">{{number}}</td>
              </tr>
              <tr>
              <td class="labela">Grad:</td>
              <td><input class="form-control" type="text" placeholder="Grad" v-model="restaurant.address.city"  v-bind:value="city">{{city}}</td>
              </tr>
              <tr>
              <td class="labela">Postanski broj:</td>
              <td><input class="form-control" type="number" placeholder="Postanski broj" v-model="restaurant.address.zipCode"  v-bind:value="zip">{{zip}}</td>
              </tr>
              <tr>
              <td class="buttonForm"><button type="button" class="btn btn-success" v-on:click="changeRestaurant">Sacuvaj</button></td>
              <td class="buttonForm"><button type="button" class="btn btn-success" v-on:click="otkazi">Otkazi</button></td>
              </tr>
              <!--<tr>
              <td class="labela">Slika:</td>
              <td><input type="file" v-model="restaurant.link" v-bind:value="link">{{link}}</td>
              </tr>-->
          </table>
      </form>
  </div>
</div>
  `,
  methods : {
    changeRestaurant: function(event){
      event.preventDefault()
      axios.post("/WebShopREST/rest/restaurant/changeRestaurant", {
      "id": '' + this.id,
      "name":''+ this.restaurant.name, 
      "type":''+ this.restaurant.type, 
      "street":''+ this.restaurant.address.street, 
      "number":''+ this.restaurant.address.number, 
      "city":''+ this.restaurant.address.city, 
      "zipCode":''+ this.restaurant.address.zipCode,
      "link":''+ this.restaurant.link, 
      "managerId":''+ this.restaurant.managerId})
      .then(
        response => {
          router.push(`/`);
        } 
      )
      .catch()
    },
    otkazi: function(event){
      event.preventDefault()
      router.push(`/`);
    },
    dodajMenadzera: function(event){
      event.preventDefault()
      router.push(`/dodajMenadzera`);
    }
  },
  mounted(){
      axios.get("/WebShopREST/rest/user/getAvailableManagers")
      .then( response => {
          this.managers = response.data
      })
      .catch(function(error){
          console.log(error)
      }),
      this.id = this.$route.query.id,
      axios.post("/WebShopREST/rest/restaurant/getRestaurant", this.id)
      .then( response => {
          this.restaurant = response.data
      })
      .catch(function(error){
          console.log(error)
      }),
      this.id = this.$route.query.id,
      axios.post("/WebShopREST/rest/user/getManager", this.id)
      .then( response => {
          this.manager = response.data
      })
      .catch(function(error){
          console.log(error)
      })
  }
});