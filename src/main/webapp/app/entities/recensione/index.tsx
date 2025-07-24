import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Recensione from './recensione';
import RecensioneDetail from './recensione-detail';
import RecensioneUpdate from './recensione-update';
import RecensioneDeleteDialog from './recensione-delete-dialog';

const RecensioneRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Recensione />} />
    <Route path="new" element={<RecensioneUpdate />} />
    <Route path=":id">
      <Route index element={<RecensioneDetail />} />
      <Route path="edit" element={<RecensioneUpdate />} />
      <Route path="delete" element={<RecensioneDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecensioneRoutes;
