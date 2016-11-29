(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('CommandeDetailController', CommandeDetailController);

    CommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Commande', 'Facture', 'DetailCommande', 'Chantier', 'Partenaire', 'TypeCommande'];

    function CommandeDetailController($scope, $rootScope, $stateParams, previousState, entity, Commande, Facture, DetailCommande, Chantier, Partenaire, TypeCommande) {
        var vm = this;

        vm.commande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:commandeUpdate', function(event, result) {
            vm.commande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
