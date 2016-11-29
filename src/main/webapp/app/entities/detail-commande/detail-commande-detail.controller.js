(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DetailCommandeDetailController', DetailCommandeDetailController);

    DetailCommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DetailCommande', 'Simulation', 'Commande', 'Bordereau'];

    function DetailCommandeDetailController($scope, $rootScope, $stateParams, previousState, entity, DetailCommande, Simulation, Commande, Bordereau) {
        var vm = this;

        vm.detailCommande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:detailCommandeUpdate', function(event, result) {
            vm.detailCommande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
