(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ProduitPartenaireDetailController', ProduitPartenaireDetailController);

    ProduitPartenaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProduitPartenaire', 'Bordereau'];

    function ProduitPartenaireDetailController($scope, $rootScope, $stateParams, previousState, entity, ProduitPartenaire, Bordereau) {
        var vm = this;

        vm.produitPartenaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:produitPartenaireUpdate', function(event, result) {
            vm.produitPartenaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
