(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DiagnosticChantierDialogController', DiagnosticChantierDialogController);

    DiagnosticChantierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiagnosticChantier', 'Chantier'];

    function DiagnosticChantierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DiagnosticChantier, Chantier) {
        var vm = this;

        vm.diagnosticChantier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.chantiers = Chantier.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.diagnosticChantier.id !== null) {
                DiagnosticChantier.update(vm.diagnosticChantier, onSaveSuccess, onSaveError);
            } else {
                DiagnosticChantier.save(vm.diagnosticChantier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:diagnosticChantierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDiagnostic = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
