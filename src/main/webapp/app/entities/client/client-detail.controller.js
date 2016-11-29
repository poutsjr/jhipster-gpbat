(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ClientDetailController', ClientDetailController);

    ClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Client', 'Chantier', 'AgenceClient'];

    function ClientDetailController($scope, $rootScope, $stateParams, previousState, entity, Client, Chantier, AgenceClient) {
        var vm = this;

        vm.client = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:clientUpdate', function(event, result) {
            vm.client = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
