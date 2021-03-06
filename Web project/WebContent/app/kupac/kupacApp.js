const ProfilKupacComponent = {template: '<profil-kupac></profil-kupac>'}
const IzmeniProfilComponent = {template: '<izmeniProfil-kupac></izmeniProfil-kupac>'}
const PrikaziRestoraneComponent = {template: '<restorani-kupac></restorani-kupac>'}
const KorpaComponent = {template: '<korpa-kupac></korpa-kupac>'}
const PorudzbineComponent = {template: '<porudbine-kupac></porudzbine-kupac>'}
const NedostavljeneComponent= {template: '<nedostavljene-kupac></nedostavljene-kupac>'}
const PrikazOdabranogRestorana= {template: '<odabraniRestoran-kupac></odabraniRestoran-kupac>'}
const KomentariRestorana= {template: '<komentariRestoran-kupac></komentariRestoran-kupac>'}



const router = new VueRouter({
    mode: 'hash',
    routes:[
        {path : '/', component: PrikaziRestoraneComponent},//prikaz svih restorana
        {path : '/profil', component: ProfilKupacComponent}, //profil
        {path : '/izmeniProfil', component: IzmeniProfilComponent}, //izmena profila
        {path : '/korpa', component: KorpaComponent}, //moja korpa 
        {path : '/porudzbine', component: PorudzbineComponent},//prethodne porudzbine
        {path : '/nedostavljene', component: NedostavljeneComponent}, //nedostavjnee porudzbine
        {path : '/restoran', component: PrikazOdabranogRestorana}, //prikaz restorana koji je oznacen za porucivanje
        {path : '/prikaziKomentareRestorana', component: KomentariRestorana}, //komentari za odredjeni restoran 
        
    ]
})

var kupacApp = new Vue({
    router,
    el: '#kupacGlavniPrikaz'
});