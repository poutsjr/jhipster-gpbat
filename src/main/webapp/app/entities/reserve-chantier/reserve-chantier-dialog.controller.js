(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReserveChantierDialogController', ReserveChantierDialogController);

    ReserveChantierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ReserveChantier', 'Chantier'];

    function ReserveChantierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ReserveChantier, Chantier) {
        var vm = this;

        vm.reserveChantier = entity;
        vm.clear = clear;
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
            if (vm.reserveChantier.id !== null) {
                ReserveChantier.update(vm.reserveChantier, onSaveSuccess, onSaveError);
            } else {
                ReserveChantier.save(vm.reserveChantier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:reserveChantierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
