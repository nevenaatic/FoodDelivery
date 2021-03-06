const ProfilComponent = {template: '<profil-menadzer></profil-menadzer>'}
const IzmeniProfilComponent = {template: '<izmeniProfil-menadzer></izmeniProfil-menadzer>'}
const RestoranComponent = {template: '<restoran-menadzer></restoran-menadzer>'}
const DodajArtikalComponent = {template: '<dodajArtikal-menadzer></dodajArtikal-menadzer>'}
const IzmeniArtikalComponent = {template: '<izmeniArtikal-menadzer></izmeniArtikal-menadzer>'}
const UsersComponent = {template: '<menadzer-users></menadzer-users>'}
const OrdersComponent = {template: '<porudbine-menadzer></porudbine-menadzer>'}
const CommentsComponent = {template: '<menadzer-comments></menadzer-comments>'}
const RequirementComponent = {template: '<menadzer-requirement></menadzer-requirement>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        {path : '/', component: RestoranComponent},
        {path : '/profilMenadzer', component: ProfilComponent}, //profil
        {path : '/izmeniProfilMenadzer', component: IzmeniProfilComponent}, //izmena profila
        {path : '/dodajArtikal', component: DodajArtikalComponent},
        {path : '/izmeniArtikal', component: IzmeniArtikalComponent},
        {path : '/korisnici', component: UsersComponent},
        {path : '/porudzbine', component: OrdersComponent},
        {path : '/komentari', component: CommentsComponent},
        {path : '/zahtevi', component: RequirementComponent},
    ]
})

var menadzerApp = new Vue({ 
    router,
    el: '#menadzer'
});