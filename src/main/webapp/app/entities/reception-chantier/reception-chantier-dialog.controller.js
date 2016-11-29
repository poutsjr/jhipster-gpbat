(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReceptionChantierDialogController', ReceptionChantierDialogController);

    ReceptionChantierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReceptionChantier'];

    function ReceptionChantierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReceptionChantier) {
        var vm = this;

        vm.receptionChantier = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.receptionChantier.id !== null) {
                ReceptionChantier.update(vm.receptionChantier, onSaveSuccess, onSaveError);
            } else {
                ReceptionChantier.save(vm.receptionChantier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:receptionChantierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
