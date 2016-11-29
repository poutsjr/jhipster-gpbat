(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('SimulationDialogController', SimulationDialogController);

    SimulationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Simulation', 'DetailCommande'];

    function SimulationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Simulation, DetailCommande) {
        var vm = this;

        vm.simulation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.detailcommandes = DetailCommande.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.simulation.id !== null) {
                Simulation.update(vm.simulation, onSaveSuccess, onSaveError);
            } else {
                Simulation.save(vm.simulation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:simulationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateSimulation = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
