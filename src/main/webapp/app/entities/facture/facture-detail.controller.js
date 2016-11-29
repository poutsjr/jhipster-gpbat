(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('FactureDetailController', FactureDetailController);

    FactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Facture'];

    function FactureDetailController($scope, $rootScope, $stateParams, previousState, entity, Facture) {
        var vm = this;

        vm.facture = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:factureUpdate', function(event, result) {
            vm.facture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
