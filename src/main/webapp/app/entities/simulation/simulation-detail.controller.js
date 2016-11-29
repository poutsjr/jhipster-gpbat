(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('SimulationDetailController', SimulationDetailController);

    SimulationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Simulation', 'DetailCommande'];

    function SimulationDetailController($scope, $rootScope, $stateParams, previousState, entity, Simulation, DetailCommande) {
        var vm = this;

        vm.simulation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:simulationUpdate', function(event, result) {
            vm.simulation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
